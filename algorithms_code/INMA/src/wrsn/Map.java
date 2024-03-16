package wrsn;

import utils.Constant;
import utils.Factor;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Map {
    private int N;  // So luong sensor
    private ArrayList<Sensor> sensors;   // Tap cac sensor
    private static final Sensor BASE_STATION = new Sensor(Constant.DEFAULT_X, Constant.DEFAULT_Y, 0, 0);
    private WCE wce;
    private static final double wceThreshold = 100;

    public Map() {
        super();
        this.sensors = new ArrayList<>();
        wce = new WCE();
    }
    public Map(int N) {
        this.N = N;
        this.sensors = new ArrayList<>();
        wce = new WCE();
    }

    public WCE getWce() {
        return wce;
    }

    public void setWce(WCE wce) {
        this.wce = wce;
    }

    public int getN() {
        return N;
    }

    public void setN(int N) {
        this.N = N;
    }

    public ArrayList<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(ArrayList<Sensor> sensors) {
        this.sensors = sensors;
    }

    public Sensor getSensor(int index) {
        return sensors.get(index);
    }

    public void setSensor(int index, Sensor sensor) {
        if (index > sensors.size() - 1) sensors.add(sensor);
        else sensors.set(index, sensor);
    }

    // Tinh Khoang cach giưa hai sensor
    public double distanceCalculate(int index1, int index2) {
        double x1 = 0, y1 = 0, x2 = 0, y2 = 0;
        if (index1 == Factor.BS_INDEX) {
            x1 = Constant.DEFAULT_X;
            y1 = Constant.DEFAULT_Y;
        }
        else if (index1 == Factor.WCE_INDEX) {
            x1 = wce.getcX();
            y1 = wce.getcY();
        }
        else {
            Sensor s = sensors.get(index1);
            x1 = s.getcX();
            y1 = s.getcY();
        }

        if (index2 == Factor.BS_INDEX) {
            x2 = Constant.DEFAULT_X;
            y2 = Constant.DEFAULT_Y;
        }
        else if (index2 == Factor.WCE_INDEX) {
            x2 = wce.getcX();
            y2 = wce.getcY();
        }
        else {
            Sensor s = sensors.get(index2);
            x2 = s.getcX();
            y2 = s.getcY();
        }
        //  d = sqrt((x1-x2)^2+(y1-y2)^2)
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public double charging(int i, double remainTime) {
        Sensor s = getSensor(i);
        double additonalE = 0;
        if (WCE.U - s.getP() > 0)
            additonalE = (WCE.U - s.getP()) * remainTime;
        double oldE = s.getE();
        if (Sensor.E_MAX - oldE - additonalE < 0) {
            additonalE = Sensor.E_MAX - oldE;
            s.setE(Sensor.E_MAX);
            return additonalE / (WCE.U - s.getP());
        }
        s.setE(oldE + additonalE);
        wce.RE -= additonalE;
        return 0;
    }

    public void setWCEState(int state) {
        wce.setState(state);
    }

    public double wceLocationCalculate(int nextChargingNode, double remainTime) {
        Sensor s = new Sensor();
        double s_x, s_y;
        if (nextChargingNode != Factor.BS_INDEX) {
            s = getSensor(nextChargingNode);
            s_x = s.getcX();
            s_y = s.getcY();
        }
        else {
            s_x = Constant.DEFAULT_X;
            s_y = Constant.DEFAULT_Y;
        }

        // Tinh goc di chuyen cua WCE - alpha
        // Dau tien, tinh khoang cach tu WCE den sensor
        double distance = distanceCalculate(Factor.WCE_INDEX, nextChargingNode);
        // Tiep theo, cosin alpha = ke/huyen
        double cosin = Math.abs(s_x - wce.getcX()) / distance;


        // Tinh vi tri moi cua WCE
        // Dau tien, tinh khoang cach di duoc trong t_interval
        double movingDistance = WCE.V * remainTime;

        // Neu khoang cach ngan hon khoang cach di duoc trong t_interval, tra ve thoi gian con thua.
        if (distance < movingDistance) {
            double delta = movingDistance - distance;
            if (nextChargingNode != Factor.BS_INDEX) {
                wce.setcX(s.getcX());
                wce.setcY(s.getcY());
            }
            else {
                wce.setcX(Constant.DEFAULT_X);
                wce.setcY(Constant.DEFAULT_Y);
            }
            wce.RE -= delta * WCE.P_M;
            return delta / WCE.V;
        }
        // Tiep theo, tinh x moi dua tren cosin alpha va khoang cach di duoc
        double newX1 = wce.getcX() + cosin * WCE.V * Factor.T_INTERVAL;
        double newX2 = wce.getcX() - cosin * WCE.V * Factor.T_INTERVAL;
        // Tiep theo, tinh y moi dua tren sin alpha va khoang cach di duoc
        double newX, newY;
        double newY1 = wce.getcY() + Math.sqrt(1 - cosin * cosin) * WCE.V * Factor.T_INTERVAL;
        double newY2 = wce.getcY() - Math.sqrt(1 - cosin * cosin) * WCE.V * Factor.T_INTERVAL;
        double d1 = Math.sqrt(Math.pow(s_x - newX1, 2) + Math.pow(s_y - newY1, 2));
        double d2 = Math.sqrt(Math.pow(s_x - newX1, 2) + Math.pow(s_y - newY2, 2));
        double d3 = Math.sqrt(Math.pow(s_x - newX2, 2) + Math.pow(s_y - newY1, 2));
        double d4 = Math.sqrt(Math.pow(s_x - newX2, 2) + Math.pow(s_y - newY2, 2));
        double minD = d1;
        newX = newX1;
        newY = newY1;
        if (d2 < minD) {
            minD = d2;
            newX = newX1;
            newY = newY2;
        }
        if (d3 < minD) {
            minD = d3;
            newX = newX2;
            newY = newY1;
        }
        if (d4 < minD) {
            minD = d4;
            newX = newX2;
            newY = newY2;
        }
        wce.RE -=  Math.sqrt(Math.pow(wce.getcX() - newX, 2) + Math.pow(wce.getcY() - newY, 2)) * WCE.P_M;
        wce.setcX(newX);
        wce.setcY(newY);
        return 0;
    }

    public boolean canReturnBS(int nextChargingNode) {
        Sensor s = getSensor(nextChargingNode);
        // Tinh khoang cach giua wce va base station
        double distanceToBS = distanceCalculate(Factor.WCE_INDEX, Factor.BS_INDEX);
        double distanceToSensor = distanceCalculate(Factor.WCE_INDEX, nextChargingNode);
        // Tinh nang luong can thiet de quay ve tram sac
        double eAfterCharge = wce.RE - distanceToSensor * WCE.P_M - (WCE.U - s.getP()) * Factor.T_INTERVAL;
        double eNeeded = distanceToBS * WCE.P_M;
        // Neu nang luong con lai cua MC < nang luong can thiet
        return eAfterCharge >= eNeeded; // phai tro ve tram sac ngay
    // neu khong can tro ve tram sac
    }
}
