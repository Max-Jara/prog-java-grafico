/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jopenglejem2;

/**
 *
 * @author LAPTOP-MAX
 */
public class pscd {
    private String msj;
    private String infoLog;
    private int progSomb;
    
    public pscd(){
        msj = "";
        infoLog = "";
        progSomb = 0;
    }

    public String getMsj() {
        return msj;
    }

    public void setMsj(String m) {
        msj = m;
    }

    public String getInfoLog() {
        return infoLog;
    }

    public void setInfoLog(String il) {
        infoLog = il;
    }

    public int getProgSomb() {
        return progSomb;
    }

    public void setProgSomb(int ps) {
        progSomb = ps;
    }
}
