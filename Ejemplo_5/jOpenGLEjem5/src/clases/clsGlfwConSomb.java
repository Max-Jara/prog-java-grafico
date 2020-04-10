/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import jopenglejem5.clsVenDeHerr;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 *
 * @author PC-MAX
 */
public class clsGlfwConSomb {
    public static List<Float> alVertices = new ArrayList<>();
    public static List<Float> alColores = new ArrayList<>();
    public static int ID;
    private static long ventana;
    private static clsVenDeHerr objVDH;
    
    //Constructor
    public clsGlfwConSomb(clsVenDeHerr ven1){
        objVDH = ven1; //obtener la ventana clsVenDeHerr para gestionar sus métodos
    }
    
    private static vcd ini_GLFW(long ven, int ancho, int alto, String titulo){
        vcd objVCD = new vcd();
        clsGestMouse objCM = new clsGestMouse(); //Clase con eventos a interactuar con el Mouse
                
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        ven = glfwCreateWindow(ancho, alto, titulo, NULL, NULL);
        if(ven == NULL){
            objVCD.setMsj("Fallo al crear la ventana con GLFW");
            objVCD.setVentana(NULL);
            return objVCD;
        }
        
        //Repintado de la ventana
        glfwSetFramebufferSizeCallback(ven, (objVentana, an, al)->{
            glViewport(0, 0, an, al);
        });
        
        //Gestión del teclado
        glfwSetKeyCallback(ven, (objVentana, tecla, scancode, accion, mods) -> {
            if (tecla == GLFW_KEY_ESCAPE && accion == GLFW_RELEASE) {
                glfwSetWindowShouldClose(objVentana, true);
                objVDH.dispose(); //cerrar clase venDeHerr
            }
        });
        
        //Gestión del Mouse
        glfwSetCursorPosCallback(ven, (long objVentana, double xPos, double yPos)->{
            /*xPos y yPos son variables enfrascadas en glfwSetCursorPosCallback, por lo que en esta
             sección sería dificil enviar los valores de xPos y yPos hacia variables locales del Main
            declarando la clase en el Main que encapsule a xPos y yPos y devuelva sus valores.
            */
            int objDib = 0;
            objCM.setX(xPos);
            objCM.setY(yPos);
            switch (objVDH.valPrim) {
                case 1: //primitiva línea
                    if(objCM.getEsPosIni() == 2){
                        //Borrando Punto y color anterior 
                        alVertices.remove(alVertices.size()-1); //borra coord x 
                        alVertices.remove(alVertices.size()-1); //borra coord y
                        alVertices.remove(alVertices.size()-1); //borra coord z
                        alColores.remove(alColores.size()-1); //borra color R
                        alColores.remove(alColores.size()-1); //borra color G
                        alColores.remove(alColores.size()-1); //borra color B

                        //Agregando Punto y color actual
                        alVertices.add(cPAV(xPos,ancho,true));
                        alVertices.add(cPAV(yPos,alto,false));
                        alVertices.add(0.0f); //pos Z no utilizada pero obligatoriamente declarada
                        alColores.add(0.0f); //R
                        alColores.add(0.0f); //G
                        alColores.add(1.0f); //B
                    }else if(objCM.getEsPosIni() == 1){
                        objCM.setEsPosIni(2);
                        //Agregando Punto y color actual
                        alVertices.add(cPAV(xPos,ancho,true));
                        alVertices.add(cPAV(yPos,alto,false));
                        alVertices.add(0.0f); //pos Z no utilizada pero obligatoriamente declarada
                        alColores.add(0.0f); //R
                        alColores.add(0.0f); //G
                        alColores.add(1.0f); //B
                    }
                    break;
            }
        });

        glfwSetMouseButtonCallback(ven,(long objVentana, int boton, int accion, int mod)->{
            if(boton == GLFW_MOUSE_BUTTON_LEFT && accion == GLFW_RELEASE){
                //Evaluar Switch de primitivas
                switch (objVDH.valPrim) {
                    case 1: //primitiva línea
                        //Agregando Punto y color actual
                        if(objCM.getEsPosIni() == 2){
                            objCM.setEsPosIni(0); //se dejo de dibujar el punto par
                        }else{//se ingresa el punto inicial impar
                            alVertices.add(cPAV(objCM.getX(),ancho,true));
                            alVertices.add(cPAV(objCM.getY(),alto,false));
                            alVertices.add(0.0f); //pos Z no utilizada pero obligatoriamente declarada
                            alColores.add(0.0f); //R
                            alColores.add(0.0f); //G
                            alColores.add(1.0f); //B
                            objCM.setEsPosIni(1);
                        }    
                        break;    
                }
            }
        });
        
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(ven,(vidmode.width() - ancho) / 2,(vidmode.height() - alto) / 2);
        
        glfwMakeContextCurrent(ven); 
        glfwSwapInterval(1); 
        
        GL.createCapabilities();
        
        objVCD.setVentana(ven); 
        return objVCD;
    }
    
    private static void Sombreador(String archVertice, String archFragmento, boolean relleno){
        //1. recuperar el código fuente de vértice y fragmento desde el directorio raíz
        String cadVertice = "", cadFragmento = "", cadLin = "";
        
        InputStream inputS = null;
        DataInputStream in = null;
        BufferedReader br = null;
        try{
            inputS = clsGlfwConSomb.class.getResourceAsStream("/clases/"+archVertice);
            in = new DataInputStream(inputS);
            br = new BufferedReader(new InputStreamReader(in));
            while ((cadLin = br.readLine())!=null) {
                cadVertice =  cadVertice + cadLin + "\n";
            }
            cadLin = "";
            inputS = clsGlfwConSomb.class.getResourceAsStream("/clases/"+archFragmento);
            in = new DataInputStream(inputS);
            br = new BufferedReader(new InputStreamReader(in));
            while ((cadLin = br.readLine())!=null) {
                cadFragmento =  cadFragmento + cadLin + "\n";
            }
        }catch(IOException ex){
            System.out.println("Error al leer archivo de compilación:\n"+
                               "Descripción: "+ex+"\n"+
                               "Verificar!!!");
            ex.printStackTrace();
            System.exit(1);
        }
        
        //2. Compilar los Sombreadores y el programa
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader,cadVertice);
        glCompileShader(vertexShader);
        verifErrorComp(vertexShader,"VERTICE");
        
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader,cadFragmento);
        glCompileShader(fragmentShader);
        verifErrorComp(fragmentShader,"FRAGMENTO");
        
        ID = glCreateProgram();
        glAttachShader(ID, vertexShader);
        glAttachShader(ID, fragmentShader);
        glLinkProgram(ID);
        verifErrorComp(ID,"PROGRAMA");
        
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        
        if(!relleno){glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);}
    }
    
    private static void verifErrorComp(int shader, String tipo){
        int success, infoLogLength;
        if(!"PROGRAMA".equals(tipo)){
            success =  glGetShaderi(shader, GL_COMPILE_STATUS);
            if(success == GL_FALSE){
                infoLogLength = glGetShaderi(shader, GL_INFO_LOG_LENGTH);
                System.out.println("Error al compilar el: "+tipo+"\n"+
                                   "Descripción: "+glGetShaderInfoLog(shader,infoLogLength)+"\n"+
                                   "Verificar!!!");
            }
        }else{
            success =  glGetProgrami(shader, GL_LINK_STATUS);
            if(success == GL_FALSE){
                infoLogLength = glGetProgrami(shader, GL_INFO_LOG_LENGTH);
                System.out.println("Error al vincular los Sombreadores:\n"+
                                   "Descripción: "+glGetProgramInfoLog(shader,infoLogLength)+"\n"+
                                   "Verificar!!!");
            }
        }
    }
    
    public static int genObjADibMod(List<Float> alVertices,List<Float> alColores){
        int pVBO = 0,cVBO = 0 , VAO = 0;
        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);
        int i;
        
        //Convertir píxeles en Coord basadas en un Vértice
        float[] vertices = new float[alVertices.size()];
        i = 0;
        for (Float f : alVertices) vertices[i++] = (f != null ? f : Float.NaN); // Evitar el NullPointerException
        float[] colores = new float[alColores.size()];
        i = 0;
        for (Float f : alColores) colores[i++] = (f != null ? f : Float.NaN); // Evitar el NullPointerException
        
        //Atributos para posición de coordenadas 3D (vértice)
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
        glBindVertexArray(0); //Liberando array
        return VAO;
    }
    
    public long ini_ventana(int ancho, int alto, String titulo){
        //Se Inicializa GLFW, si todo es correcto; se crea la ventana
        vcd venConDat = new vcd();
        venConDat = ini_GLFW(ventana,ancho, alto, titulo);
        if (!"".equals(venConDat.getMsj())) {
            System.out.println(venConDat.getMsj());
            glfwTerminate(); 
            return NULL;
        }
        else {
            ventana = venConDat.getVentana();
            Sombreador("verticeSombMod.vs","fragmentoSomb.fs",true);
        }
        return ventana;
    }
    
    private static float cPAV(double pos,int med,boolean eje){//(c)onvertir (P)ixeles (A) coord de tipo (V)értice 
        float con;
        if(eje){
            con = (float)((2*pos)/med-1); //fórmula matemática para el eje x
        }else{
            con = (float)(1-(2*pos)/med); //fórmula matemática para el eje y
        }
        return con;
    }
}
