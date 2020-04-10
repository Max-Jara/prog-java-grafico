/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jopenglejem4;
import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;


/**
 *
 * @author PC-MAX
 */
public class JOpenGLEjem4 {
    /**
     * @param args the command line arguments
     */
       
    public static void main(String[] args) {
        clsVenDeHerr objVDH = new clsVenDeHerr();
        
        objVDH.setVisible(true);
        
        final int _Ancho = 1024;
        final int _Alto = 720;
        long ventana = NULL;
        clsGlfwConSomb objGCS = new clsGlfwConSomb(objVDH);
        clsCoordMouse objCM = new clsCoordMouse(); //Forma de recuperar coordenadas del Mouse
        List<Float> alVertices = new ArrayList<>();
        List<Float> alColores = new ArrayList<>();
        int objDib, i;
        try {
            ventana = objGCS.ini_ventana(_Ancho, _Alto, "Ejem 4: OpenGL con Java - Primitivas Con Mouse");
            if(ventana != NULL){
                
                //Se genera el dibujo con el arreglo de puntos 
                objDib = objGCS.genObjADibMod(alVertices,alColores);
                
                glfwSetCursorPosCallback(ventana, (long objVentana, double xPos, double yPos)->{
                    /*xPos y yPos son variables enfrascadas en glfwSetCursorPosCallback, por lo que en esta
                     sección sería dificil enviar los valores de xPos y yPos hacia variables locales del Main
                    declarando la clase en el Main que encapsule a xPos y yPos y devuelva sus valores.
                    */
                    if(!alVertices.isEmpty() && objCM.getEsPosIni() != 1){
                        alVertices.remove(alVertices.size()-1); //borra coord x 
                        alVertices.remove(alVertices.size()-1); //borra coord y
                        alVertices.remove(alVertices.size()-1); //borra coord z
                        
                        //Borrando color de punto
                        alColores.remove(alColores.size()-1); //borra color R
                        alColores.remove(alColores.size()-1); //borra color G
                        alColores.remove(alColores.size()-1); //borra color B
                    }else{
                        objCM.setEsPosIni(0);
                    }
                    alVertices.add(objGCS.cPAV(xPos,_Ancho,true));
                    alVertices.add(objGCS.cPAV(yPos,_Alto,false));
                    alVertices.add(0.0f); //pos Z no utilizada pero obligatoriamente declarada
                    
                    //Agregando color al punto
                    alColores.add(0.0f); //R
                    alColores.add(0.0f); //G
                    alColores.add(1.0f); //B
                    objCM.setX(xPos);
                    objCM.setY(yPos);
                });
                
                glfwSetMouseButtonCallback(ventana,(long objVentana, int boton, int accion, int mod)->{
                    if(boton == GLFW_MOUSE_BUTTON_LEFT && accion == GLFW_RELEASE){
                        objCM.setEsPosIni(1);
                    }
                });
                
                //Bucle de renderizado
                while (!glfwWindowShouldClose(ventana)) {
                    glClearColor(1.0f, 1.0f, 1.0f, 0.0f);//color de fondo - Blanco
                    glClear(GL_COLOR_BUFFER_BIT); // limpiar el FrameBuffer
                    
                    //Se genera el dibujo con la lista de arreglos de puntos según el movimiento del mouse
                    objDib = objGCS.genObjADibMod(alVertices,alColores); 
                    
                    //Se llama al evento use para usar el Programa creado, dibujar el objeto objDib y Activar el búfer
                    objGCS.use(objDib,alVertices.size(),ventana);
                    glfwPollEvents(); //capturar cualquier interacción del usuario con la ventana
                }
                glDeleteVertexArrays(objDib);
                glDeleteBuffers(objDib);
                glfwFreeCallbacks(ventana); //Liberar las llamadas que interactuen con la ventana
                glfwDestroyWindow(ventana); //Liberar memoria de la ventana creada
            }
        } finally {
            glfwTerminate(); //limpia y termina la aplicación
        }
    }    
}
