/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jopenglejem5;

import clases.clsGlfwConSomb;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 *
 * @author PC-MAX
 */
public class JOpenGLEjem5 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final int _Ancho = 1024;
        final int _Alto = 720;
        long ventana = NULL;
        clsVenDeHerr objVDH = new clsVenDeHerr();
        clsGlfwConSomb objGCS = new clsGlfwConSomb(objVDH);
        int objDib;
        
        objVDH.setVisible(true);
        try {
            ventana = objGCS.ini_ventana(_Ancho, _Alto, "Ejem 5: OpenGL con Java - Primitivas + Transformaciones, usando el Mouse");
            if(ventana != NULL){
                glClearColor(1.0f, 1.0f, 1.0f, 0.0f);//configurar color de fondo - Blanco
                
                //Bucle de renderizado
                while (!glfwWindowShouldClose(ventana)) {
                    glClear(GL_COLOR_BUFFER_BIT); // limpiar el FrameBuffer
                    objDib = objGCS.genObjADibMod(objGCS.alVertices,objGCS.alColores);
                    glUseProgram(objGCS.ID);
                    glBindVertexArray(objDib); //vincular el objeto a dibujar con su Arreglo de vértices
                    if((objGCS.alVertices.size()/3)%2 == 0){//verificar si lleva siempre par de puntos (de 2 en 2)
                        glDrawArrays(GL_LINES, 0,objGCS.alVertices.size()/3);//uso de puntos (vértices basados en 3 coord)
                        
                        //Transformación de traslación
                        Mat4 tras = new Mat4(1.0f,0.0f,0.0f,0.0f,0.0f,1.0f,0.0f,0.0f,0.0f,0.0f,1.0f,0.0f,0.0f,0.0f,0.0f,1.0f);
                        tras = tras.translate(new Vec3(objVDH.vTrasX, objVDH.vTrasY, 0.0f));
                        
                        //Transformación de rotación
                        Mat4 rota = new Mat4(1.0f,0.0f,0.0f,0.0f,0.0f,1.0f,0.0f,0.0f,0.0f,0.0f,1.0f,0.0f,0.0f,0.0f,0.0f,1.0f);
                        //trans = trans.rotate(-1*objVDH.vRot, new Vec3(0.0f,0.0f,1.0f)); //rotación sobre eje Z (horario)
                        rota = rota.rotate(objVDH.vRot, new Vec3(0.0f,0.0f,1.0f)); //rotación sobre eje Z (antihorario)
                        
                        //Transformación de Escalación 
                        Mat4 esca = new Mat4(1.0f,0.0f,0.0f,0.0f,0.0f,1.0f,0.0f,0.0f,0.0f,0.0f,1.0f,0.0f,0.0f,0.0f,0.0f,1.0f);
                        esca = esca.scale(objVDH.vEsc,objVDH.vEsc,0.0f); //OJO!!! vEsc debe valer 1.0f = tamaño original
                        
                        //Llevar las transformaciones al área de dibujo
                        int transformLoc = glGetUniformLocation(objGCS.ID, "transform");
                        Mat4 trans = tras.mul(rota).mul(esca); //Unir la traslación, rotación y Escalación (vectorialmente)
                        if (transformLoc > -1) {// ya declarado
                            FloatBuffer MatrizTrans = BufferUtils.createFloatBuffer(trans.toFa_().length);
                            MatrizTrans.put(trans.toFa_());
                            MatrizTrans.flip();
                            glUniformMatrix4fv(transformLoc, false, MatrizTrans);
                        }
                    }    
                    glfwSwapBuffers(ventana); //activar el doble búfer
                    glDeleteVertexArrays(objDib);
                    glDeleteBuffers(objDib);
                    glLineWidth(objVDH.valGrosor);//grosor de línea
                    glfwPollEvents(); //capturar cualquier interacción del usuario con la ventana
                }
                glfwFreeCallbacks(ventana); //Liberar las llamadas que interactuen con la ventana
                glfwDestroyWindow(ventana); //Liberar memoria de la ventana creada
            }
        } finally {
            glfwTerminate(); //limpia y termina la aplicación
        }
    }
    
}
