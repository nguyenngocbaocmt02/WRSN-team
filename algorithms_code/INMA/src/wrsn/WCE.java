package wrsn;

import utils.Constant;
import utils.Factor;

public class WCE {
    public static double E_MC = 108000;   // J
    public static final double P_M = 1;   // W/s
    public static double V = 5;   // m/s
    public static double U = 5;   // W
    private double cX;  // Hoanh do cua WCE hien tai
    private double cY;  // Tung do cua WCE hien tai
    private int state; // trang thai hien tai
    public double RE = E_MC;

    public WCE() {
        this.cX = Constant.DEFAULT_X;
        this.cY = Constant.DEFAULT_Y;
        this.state = Factor.IDLE;
    }

    public double getcX() {
        return cX;
    }

    public void setcX(double cX) {
        this.cX = cX;
    }

    public double getcY() {
        return cY;
    }

    public void setcY(double cY) {
        this.cY = cY;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
