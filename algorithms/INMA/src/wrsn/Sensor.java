package wrsn;

import utils.Factor;

import java.util.Random;

public class Sensor {
    public static final double E_MAX = 10800;    // Nang luong toi da cua sensor
    public static final double E_MIN = 540;  // Nang luong toi thieu cua sensor
    public static final double E_THRESHOLD = 0.5 * E_MAX;  // Nang luong nguong sac cua sensor
    private double cX;  // Hoanh do cua sensor
    private double cY;  // Tung do cua sensor
    private double p;   //  Cong suat tieu thu cua sensor
    private double E;   // nang luong con lai cua sensor

    public Sensor() {
        super();
    }
    public Sensor(double cX, double cY, double p, double e) {
        this.cX = cX;
        this.cY = cY;
        this.p = p;
        E = e;
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

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getE() {
        return E;
    }

    public void setE(double e) {
        E = e;
    }

    public void calculateE(double time) {
//        double pVariant = - Factor.P_BOUND +  (Factor.P_BOUND + Factor.P_BOUND) * new Random().nextDouble();
//        if (pVariant > Factor.P_BOUND || pVariant < - Factor.P_BOUND) System.out.println("pVariant = " + pVariant);
        E -= (p) * time;
    }
}
