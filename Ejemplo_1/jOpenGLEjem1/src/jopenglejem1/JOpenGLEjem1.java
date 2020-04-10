/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jopenglejem1;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*; //(static) permite usar directamente el nombre de los métodos de la clase Callbacks 
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 *
 * @author PC-MAX
 */
public class JOpenGLEjem1 {

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
            glViewport(0, 0, an, al);
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
        objVCD.setVentana(ventana); //Asignar la ventana creada al objeto objVCD para su retorno (en este caso, sin obs)
        return objVCD;
    }    
    
    public static void main(String[] args) {
        long ventana = NULL;
        vcd venConDat = new vcd();
        try {
            //Se inicializa GLFW, asimismo si todo es correcto, se crea la ventana
            venConDat = ini_GLFW(ventana,1024, 720, "Ejem 1: OpenGL con Java");
            if (!"".equals(venConDat.getMsj())) {
                System.out.println(venConDat.getMsj()); //imprime el error cometido
                glfwTerminate(); //limpia y termina la aplicación
            }
            else {
                ventana = venConDat.getVentana();
            }
            GL.createCapabilities(); //Linea elemental para hacer interactuar el contexto openGL con GLFW
            glClearColor(1.0f, 0.0f, 0.0f, 0.0f); //Configurar color de fondo 

            while (!glfwWindowShouldClose(ventana)) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // limpiar el FrameBuffer
                glfwSwapBuffers(ventana); //activar el doble búfer
                glfwPollEvents(); //capturar cualquier interacción del usuario con la ventana
            }
            glfwFreeCallbacks(ventana); //Liberar las llamadas que interactuen con la ventana
            glfwDestroyWindow(ventana); //Liberar memoria de la ventana creada
        } finally {
            glfwTerminate(); //limpia y termina la aplicación
        }
    }
    
}
