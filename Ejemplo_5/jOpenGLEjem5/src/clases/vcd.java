/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author LAPTOP-MAX
 */
public class vcd {
    private String msj;
    private long ventana;
    
    public vcd(){
        msj = "";
        ventana = 0;
    }

    public String getMsj() {
        return msj;
    }

    public void setMsj(String m) {
        msj = m;
    }

    public long getVentana() {
        return ventana;
    }

    public void setVentana(long v) {
        ventana = v;
    }
}
