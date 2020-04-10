/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jopenglejem3;
import java.nio.FloatBuffer;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;
/**
 *
 * @author LAPTOP-MAX
 */
public class JOpenGLEjem3 {

    /**
     * @param args the command line arguments
     */
    
    private static vcd ini_GLFW(long ventana, int ancho, int alto, String titulo){
        vcd objVCD = new vcd();
        
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        
        ventana = glfwCreateWindow(ancho, alto, titulo, NULL, NULL);
        if(ventana == NULL){
            objVCD.setMsj("Fallo al crear la ventana con GLFW");
            objVCD.setVentana(NULL);
            return objVCD;
        }
        
        glfwSetFramebufferSizeCallback(ventana, (objVentana, an, al)->{
            glViewport(0, 0, an, al);
        });
        
        glfwSetKeyCallback(ventana, (objVentana, tecla, scancode, accion, mods) -> {
            if (tecla == GLFW_KEY_ESCAPE && accion == GLFW_RELEASE) {
                glfwSetWindowShouldClose(objVentana, true); 
            }
        });
        
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(ventana,(vidmode.width() - ancho) / 2,(vidmode.height() - alto) / 2);
        
        glfwMakeContextCurrent(ventana); 
        glfwSwapInterval(1); 
        
        GL.createCapabilities();
        
        objVCD.setVentana(ventana); 
        return objVCD;
    }
    
    private static int genObjaDib(float vertices[],float colores[]){
        int pVBO = 0,cVBO = 0 , VAO = 0;
        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);
        
        //Búfer y Atributos para posición de coordenadas 3D (vértice)
        pVBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, pVBO);
        FloatBuffer vbo = BufferUtils.createFloatBuffer(vertices.length); //vertex buffer object
        vbo.put(vertices);
        vbo.flip();
        glBufferData(GL_ARRAY_BUFFER, vbo, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0); //liberando búfer vinculado
        
        //Atributo para posición de colores RGB
        cVBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, cVBO);
        FloatBuffer cbo = BufferUtils.createFloatBuffer(colores.length); //color buffer object
        cbo.put(colores);
        cbo.flip();
        glBufferData(GL_ARRAY_BUFFER, cbo, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, 0); //liberando búfer vinculado
        
        //Liberando array
        glBindVertexArray(0);
        return VAO;
    }
    
    public static void main(String[] args) {
        long ventana = NULL;
        vcd venConDat = new vcd();
        int objDib;
        try {
            //Se Inicializa GLFW, si todo es correcto; se crea la ventana
            venConDat = ini_GLFW(ventana,1024, 720, "Ejem 3: OpenGL con Java - Colores y Objetos");
            if (!"".equals(venConDat.getMsj())) {
                System.out.println(venConDat.getMsj());
                glfwTerminate(); 
            }
            else {
                ventana = venConDat.getVentana();
            }
            
            //Se instancia la clase Sombreador y se compilan los sombreadores y el Programa
            Sombreador objSomb = new Sombreador("verticeSomb.vs","fragmentoSomb.fs",false);
            
            //Se ingresa las coordenadas y colores para dibujar la(s) primitiva(s)
            float vertices[] = {
                0.5f, -0.5f, 0.0f,
               -0.5f, -0.5f, 0.0f,
               
               -0.5f, -0.5f, 0.0f,
                0.0f,  0.5f, 0.0f,
                
                0.0f,  0.5f, 0.0f,
                0.5f, -0.5f, 0.0f 
            };
            
            float colores[] = {
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f    
            };
            
            //Se genera el dibujo con el arreglo de puntos
            objDib = genObjaDib(vertices,colores);
            
            //Bucle de renderizado
            while (!glfwWindowShouldClose(ventana)) {
                glClearColor(1.0f, 1.0f, 1.0f, 0.0f);//color de fondo - Blanco
                glClear(GL_COLOR_BUFFER_BIT); // limpiar el FrameBuffer
                objSomb.use();//Se llama al evento use para usar el Programa creado
                glBindVertexArray(objDib);
                glDrawArrays(GL_LINE_STRIP, 0, 6);//uso de 6 puntos (3 pares de puntos) y el utimo lo une con el primero
                glLineWidth(2.0f);//grosor de línea
                
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
