/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jopenglejem2;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*; //(static) permite usar directamente el nombre de los métodos de la clase Callbacks 
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*; //Para los create vertex
import static org.lwjgl.system.MemoryUtil.*;

/**
 *
 * @author LAPTOP-MAX
 */
public class JOpenGLEjem2 {

    /**
     * @param args the command line arguments
     */
    private static vcd ini_GLFW(long ventana, int ancho, int alto, String titulo){
        vcd objVCD = new vcd();
        
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE,GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); //la ventana podrá ser redimensionada
        
        //Se construye la ventana  para GLFW
        ventana = glfwCreateWindow(ancho, alto, titulo, NULL, NULL);
        if(ventana == NULL){
            objVCD.setMsj("Fallo al crear la ventana para GLFW");
            objVCD.setVentana(NULL);
            return objVCD;
        }
                
        //Se configura la redimensión de la ventana usando a glViweport()
        glfwSetFramebufferSizeCallback(ventana, (objVentana, an, al) -> {
            GL11.glViewport(0, 0, an, al);
        });
        
        //Se configura la interacción de la ventana con el teclado, el cual se verifica con glfwPollEvents()
        glfwSetKeyCallback(ventana, (objVentana, tecla, scancode, accion, mods) -> {
            if (tecla == GLFW_KEY_ESCAPE && accion == GLFW_RELEASE) {
                glfwSetWindowShouldClose(objVentana, true); // Se indica instrucción de cierre al darle el valor "true"
            }
        });
        
        //Se obtiene la resolución del monitor principal para mostrar la ventana en forma centrada
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(ventana,(vidmode.width() - ancho) / 2,(vidmode.height() - alto) / 2); //Centrar ventana
        
        glfwMakeContextCurrent(ventana); //Colocar la ventana como hilo de proceso actual
        glfwSwapInterval(1); // Activar la sincronización vertical en el proceso actual(1), para evitar el parpadeo entre búfers
        
        GL.createCapabilities(); //Se procede a vincular la interación del contexto GLFW con openGL
            
        objVCD.setVentana(ventana); //Asignar la ventana creada al objeto objVCD para su retorno (en este caso, sin obs)
        return objVCD;
    }
    
    private static pscd genProgSombreador(String color, boolean relleno){
        pscd objPSCD = new pscd();
        
        //Se definen las variables de compilación para un vértice y un fragmento
        String vertexShaderSource = 
        "#version 330 core\n"+
        "layout(location = 0) in vec3 aPos;\n"+
        "void main()\n"+
        "{\n"+
            "gl_Position = vec4(aPos.x,aPos.y,aPos.z,1.0);\n" +
        "}\0";
        
        String fragmentShaderSource = 
        "#version 330 core\n"+
        "out vec4 FragColor;\n"+
        "void main()\n"+
        "{\n"+
            "FragColor = vec4("+color+");\n" +
        "}\n\0";
        
        //Se construye y compila el Programa Sombreador
        //---------------------------------------------    
        //Vértice sombreador
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);
        
        //Verificar si el vertexShader tuvo éxito al compilarse
        int success = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            objPSCD.setMsj("ERROR:Vértice sombreador::Falló la compilación\n");
            int infoLogLength = glGetShaderi(vertexShader, GL_INFO_LOG_LENGTH);
            objPSCD.setInfoLog(glGetShaderInfoLog(vertexShader, infoLogLength));
            return objPSCD;
        }
        
        //Fragmento sombreador
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        
        //Verificar si el fragmentShader tuvo éxito al compilarse
        success = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            objPSCD.setMsj("ERROR:Fragmento sombreador::Falló la compilación\n");
            int infoLogLength = glGetShaderi(fragmentShader, GL_INFO_LOG_LENGTH);
            objPSCD.setInfoLog(glGetShaderInfoLog(fragmentShader, infoLogLength));
            return objPSCD;
        }
        
        //Vincular Sombreadores
        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        
        //Verificar si el shaderProgram tuvo éxito al compilarse
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE){
            objPSCD.setMsj("ERROR:Vinculación de sombreadores::Falló la compilación\n");
            int infoLogLength = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            objPSCD.setInfoLog(glGetProgramInfoLog(shaderProgram, infoLogLength));
            glDeleteShader(vertexShader);
            glDeleteShader(fragmentShader);
            return objPSCD;
        }
        glDeleteShader(vertexShader); //liberar memoria del contenido del vértice
        glDeleteShader(fragmentShader); //liberar memoria del contenido del Fragmento
        
        if(!relleno){glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);}
        
        objPSCD.setMsj("");
        objPSCD.setInfoLog("");
        objPSCD.setProgSomb(shaderProgram);
        return objPSCD;
    }
    
    private static int genObjLinADib(float xini,float yini,float xfin,float yfin){
        float vertices[] = {
            xini, yini, 0.0f,
            xfin, yfin, 0.0f
        };
         
        int VAO = 0, VBO = 0;
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        
        FloatBuffer verticesBufferObject = BufferUtils.createFloatBuffer(vertices.length); // crear contenedor FloatBuffer con el tamaño del arreglo
        verticesBufferObject.put(vertices); // poner los datos del arreglo en el contenedor
	verticesBufferObject.flip(); // Limpiar el índice del contenedor (método similar a trim() de cadenas)
        
        glBufferData(GL_ARRAY_BUFFER, verticesBufferObject, GL_STATIC_DRAW);
	glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        return VAO;
    }
    
    public static void main(String[] args) {
        long ventana = NULL;
        vcd venConDat = new vcd();
        pscd progSombConDat = new pscd();
        int progSomb = 0;
        int objDib = 0;
        try {
            //Se inicializa GLFW, asimismo si todo es correcto, se crea la ventana
            venConDat = ini_GLFW(ventana,1024, 720, "Ejem 2: OpenGL con Shaders - Java");
            if (!"".equals(venConDat.getMsj())) {
                System.out.println(venConDat.getMsj()); //imprime el error cometido
                glfwTerminate(); //limpia y termina la aplicación
            }
            else {
                ventana = venConDat.getVentana();
            }
            
            //Se construye el Programa Sombreador (ShaderProgram) asignando el color de línea
            progSombConDat = genProgSombreador("0.0f, 0.0f, 1.0f, 1.0f", false);
            if (!"".equals(progSombConDat.getMsj())) {
                System.out.println(progSombConDat.getMsj()); //imprime el msg del error cometido
                System.out.println(progSombConDat.getInfoLog()); //imprime el log del error cometido
                glfwTerminate(); //limpia y termina la aplicación
            }
            else {
                progSomb = progSombConDat.getProgSomb();
            }
            
            objDib = genObjLinADib(-0.5f, -0.5f, 0.5f, 0.5f);
            
            while (!glfwWindowShouldClose(ventana)) {
                glClearColor(0.5f, 0.5f, 0.5f, 0.0f); //Configurar color de fondo 
                glClear(GL_COLOR_BUFFER_BIT); // limpiar el FrameBuffer
                glUseProgram(progSomb);
                glBindVertexArray(objDib);
                glDrawArrays(GL_LINE_STRIP, 0, 2);
                glLineWidth(3.0f);
                
                glfwSwapBuffers(ventana); //activar el doble búfer
                glfwPollEvents(); //capturar cualquier interacción del usuario con la ventana
            }
            glDeleteVertexArrays(objDib);
            glDeleteBuffers(objDib);
            
            glfwFreeCallbacks(ventana); //Liberar las llamadas que interactuen con la ventana
            glfwDestroyWindow(ventana); //Liberar memoria de la ventana creada
        } finally {
            glfwTerminate(); //limpia y termina la aplicación
        }
    }
}
