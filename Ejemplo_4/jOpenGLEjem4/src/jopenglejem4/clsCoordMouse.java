/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jopenglejem4;

/**
 *
 * @author PC-MAX
 */
public class clsCoordMouse {
    private double x;
    private double y;
    private int posIni;

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }
    
    public int getEsPosIni() {
        return posIni;
    }

    /**
     * @param y the y to set
     */
    public void setEsPosIni(int pos) {
        this.posIni = pos;
    }
}
