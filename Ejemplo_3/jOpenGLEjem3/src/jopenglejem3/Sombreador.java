/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jopenglejem3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.opengl.GL30.*;

/**
 *
 * @author LAPTOP-MAX
 */
public class Sombreador {
    private int ID;
    
    //Constructor de la clase
    public Sombreador(String archVertice, String archFragmento, boolean relleno){
        //1. recuperar el código fuente de vértice y fragmento desde el directorio raíz
        String cadVertice = "", cadFragmento = "", cadLin = "";
        File arch = null;
        FileReader fr = null;
        BufferedReader br = null;
        try{
            arch = new File("src\\jopenglejem3\\"+archVertice);
            fr = new FileReader (arch.getAbsolutePath());
            br = new BufferedReader(fr);
            while ((cadLin = br.readLine())!=null) {
                cadVertice =  cadVertice + cadLin + "\n";
            }
            cadLin = "";
            arch = new File("src\\jopenglejem3\\"+archFragmento);
            fr = new FileReader (arch.getAbsolutePath());
            br = new BufferedReader(fr);
            while ((cadLin = br.readLine())!=null) {
                cadFragmento =  cadFragmento + cadLin + "\n";
            }
        }catch(IOException ex){
            System.out.println("Error al leer archivo de compilación:\n"+
                               "Descripción: "+ex+"\n"+
                               "Verificar!!!");
            ex.printStackTrace();
            System.exit(1);
        }finally{
            try{                    
                if( null != fr ){   
                    fr.close();     
                }                  
            }catch (Exception ex2){ 
                ex2.printStackTrace();
            }
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
    
    public void use(){
        glUseProgram(ID);
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
}
