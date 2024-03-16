package experiments;

import utils.Constant;
import utils.Factor;
import wrsn.Map;
import wrsn.Sensor;
import wrsn.WCE;

import java.util.ArrayList;
import java.util.HashMap;

public class INMA extends Algorithm {
    private int ST = 0; // tong timestamp
    private double timeIntervalSum = Factor.T_INTERVAL;
    double e_move = 0;
    double e_mc = 0;
    int batteryTimes = 0;
    @Override
    public void execute(Map map, Factor factor) {
        int N = map.getN(); // so chieu bai toan
        int timestamp = 0;
        double[] R = new double[N]; // cong suat tieu thu real time
        ArrayList<Integer> S = new ArrayList<>(); // Service pool
        Message[] messages = new Message[N]; // danh sach cac request
        long startTime = System.nanoTime();
        while (timestamp < Factor.T) {
            for (int i = 0; i < N; i++) {
                Sensor s = map.getSensor(i);
                s.calculateE(timestamp == 0 ? timestamp : timeIntervalSum);    // tinh nang luong con lai cua sensor i
                double r = s.getP();
                if (timestamp != 0) {
                    R[i] = (R[i] * ST + r * timestamp) / (ST + timestamp);
                }
                else {
                    R[i] = r;
                }
                int ID = i;
                double RE = s.getE();
                double t = timestamp;
                if (RE < Sensor.E_MIN) {
                    if (!deadNodes.contains(i)) {
                        deadNodes.add(i);
                    }
                }
                messages[i] = new Message(ID, RE, t, RE - Sensor.E_THRESHOLD < 0);
                if (messages[i].isUrg()) {
                    if (!S.contains(i)) {
                        S.add(i);
                    }
                }
            }
            if (!S.isEmpty() ) {
                inmaSelection(map, S, R, messages, timestamp);
            }
            if (timeIntervalSum < Factor.T_INTERVAL) timeIntervalSum = Factor.T_INTERVAL;
            timestamp += Math.ceil(timeIntervalSum / Factor.T_INTERVAL) * Factor.T_INTERVAL;
            ST += timestamp;
        }
        long elapsedTime = System.nanoTime() - startTime;
        executedTime = elapsedTime / 10e6;
        eMoveRatio = e_move/e_mc;
    }

    private void inmaSelection(Map map, ArrayList<Integer> S, double[] R, Message[] messages, int t) {
        HashMap<Integer, Double> latencies = new HashMap<>();   // tap do tre sac chap nhan duoc cua cac cam bien gui request
        HashMap<Integer, Integer> deaths = new HashMap<>();   // tap do tre sac chap nhan duoc cua cac cam bien gui request
        ArrayList<Integer> Z = new ArrayList<>();   // tap ung cu vien
        int K = 0;
        int nextChargingNode = 0; // // ung cu vien so mot
        double best_t_mc_i = 0;
        double best_t_charging_i = 0;
        double best_t_charge = 0;

        ArrayList<Integer> depletionNodes = new ArrayList<>();
        for (int i : S) {
            double RE = messages[i].getRE();
            double ts = messages[i].getT();
            double r = map.getSensor(i).getP();
            double latency = (RE - Sensor.E_MIN) / R[i] + ts - t;
            if (latency <= 0) {
                depletionNodes.add(i);
                if (!deadNodes.contains(i)) {
                    deadNodes.add(i);
                }
            }
            else if (r > WCE.U) {
                depletionNodes.add(i);
            }
            else if (!deadNodes.contains(Integer.valueOf(i))) {
                    latencies.put(i, latency);
                    K ++;
            }
        }
        for (int i : depletionNodes) {
            S.remove(Integer.valueOf(i));
        }
        for (int i : deadNodes) {
            S.remove(Integer.valueOf(i));
        }
        if (S.isEmpty()) {
            timeIntervalSum = Factor.T_INTERVAL;
            return;
        }
        for (int s = 0; s < K; s++) {
            int i = S.get(s);
            double RE = messages[i].getRE();
            double ts = messages[i].getT();
            int deaths_i = 0;
            for (int x = 0; x < K; x++) {
                int j = S.get(x);
                double t_mc_i = map.distanceCalculate(Factor.WCE_INDEX, i) / WCE.V;
                double t_i_j = map.distanceCalculate(i, j) / WCE.V;
                double t_charging_i = (Sensor.E_MAX - RE - R[i] * (t - ts + t_mc_i)) / WCE.U;
                double w_i_j = t_mc_i + t_charging_i + t_i_j;
                if (latencies.get(j) < w_i_j)
                    deaths_i ++;
            }
            deaths.put(i, deaths_i);
            if (deaths_i == 0) {
                Z.add(i);
            }
        }

        if (!Z.isEmpty()) {
            double min_t_charge  = Double.MAX_VALUE;
            for (int i : Z) {
                double RE = messages[i].getRE();
                double ts = messages[i].getT();
                double t_mc_i = map.distanceCalculate(Factor.WCE_INDEX, i) / WCE.V;
                double t_charging_i = (Sensor.E_MAX - RE - R[i] * (t - ts + t_mc_i)) / WCE.U;
                double t_charge = t_mc_i + t_charging_i;
                if (t_charge < min_t_charge) {
                    min_t_charge = t_charge;
                    nextChargingNode = i;
                    best_t_mc_i = t_mc_i;
                    best_t_charging_i = t_charging_i;
                    best_t_charge = t_charge;
                }
            }
            if (mcCanGetBackToBS(map, nextChargingNode)) {
                timeIntervalSum = best_t_charge;
                Sensor s = map.getSensor(nextChargingNode);
                e_move = best_t_mc_i * WCE.P_M;
                e_mc = e_move + best_t_charging_i * WCE.U;
                sumMove += e_move;
                sumCharge += best_t_charging_i * WCE.U;
                map.getWce().RE -=  e_mc;
                System.out.println("T = " + t + ", ID = " + nextChargingNode + ", p = " + s.getP() + ", E = " + s.getE() + ", T_charge =" + best_t_charge + ", WCE_RE = " + map.getWce().RE);
                map.getSensor(nextChargingNode).setE(Sensor.E_MAX);
                S.remove(Integer.valueOf(nextChargingNode));
                map.getWce().setcX(s.getcX());
                map.getWce().setcY(s.getcY());
            }
        }
        else {
            int leastDeathNode = 0;
            int minDeaths = Integer.MAX_VALUE;
            for (int s = 0; s < K; s++) {
                int i = S.get(s);
                double RE = messages[i].getRE();
                double ts = messages[i].getT();
                double t_mc_i = map.distanceCalculate(Factor.WCE_INDEX, i) / WCE.V;
                double t_charging_i = (Sensor.E_MAX - RE - R[i] * (t - ts + t_mc_i)) / WCE.U;
                double t_charge = t_mc_i + t_charging_i;
                if (deaths.get(i) < minDeaths) {
                    minDeaths = deaths.get(i);
                    leastDeathNode = i;
                    best_t_mc_i = t_mc_i;
                    best_t_charging_i = t_charging_i;
                    best_t_charge = t_charge;
                }
            }

            if (mcCanGetBackToBS(map, leastDeathNode)) {
                timeIntervalSum = best_t_charge;
                Sensor s = map.getSensor(leastDeathNode);
                e_move = best_t_mc_i * WCE.P_M;
                e_mc = e_move + best_t_charging_i * WCE.U;
                sumMove += e_move;
                sumCharge += best_t_charging_i * WCE.U;
                map.getWce().RE -= e_mc;
                System.out.println("T = " + t + ", ID = " + leastDeathNode + ", p = " + s.getP() + ", E = " + s.getE() + ", T_charge =" + best_t_charge + ", WCE_RE = " + map.getWce().RE);
                map.getSensor(leastDeathNode).setE(Sensor.E_MAX);
                S.remove(Integer.valueOf(leastDeathNode));
                map.getWce().setcX(s.getcX());
                map.getWce().setcY(s.getcY());
            }
            else {
                int closestNode = 0;
                double minDistance = Double.MAX_VALUE;
                for (int s = 0; s < K; s++) {
                    int i = S.get(s);
                    double RE = messages[i].getRE();
                    double ts = messages[i].getT();
                    double t_mc_i = map.distanceCalculate(Factor.WCE_INDEX, i) / WCE.V;
                    double t_charging_i = (Sensor.E_MAX - RE - R[i] * (t - ts + t_mc_i)) / WCE.U;
                    double t_charge = t_mc_i + t_charging_i;
                    double distance = map.distanceCalculate(Factor.WCE_INDEX, i);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestNode = i;
                        best_t_mc_i = t_mc_i;
                        best_t_charging_i = t_charging_i;
                        best_t_charge = t_charge;
                    }
                }
                if (mcCanGetBackToBS(map, closestNode)) {
                    timeIntervalSum = best_t_charge;
                    Sensor s = map.getSensor(closestNode);
                    e_move = best_t_mc_i * WCE.P_M;
                    e_mc = e_move + best_t_charging_i * WCE.U;
                    sumMove += e_move;
                    sumCharge += best_t_charging_i * WCE.U;
                    map.getWce().RE -= e_mc;
                    System.out.println("T = " + t + ", ID = " + closestNode + ", p = " + s.getP() + ", E = " + s.getE() + ", T_charge =" + best_t_charge + ", WCE_RE = " + map.getWce().RE);
                    s.setE(Sensor.E_MAX);
                    S.remove(Integer.valueOf(closestNode));
                    map.getWce().setcX(s.getcX());
                    map.getWce().setcY(s.getcY());
                }
                else {
                    timeIntervalSum = map.distanceCalculate(Factor.WCE_INDEX, Factor.BS_INDEX) / WCE.V;
                    System.out.println("T = " + t + ", replenishing, T_charge = " + timeIntervalSum + ", WCE_RE = " + map.getWce().RE);
                    e_move = timeIntervalSum * WCE.P_M;
                    e_mc = e_move;
                    sumMove += e_move;
                    map.getWce().setcX(Constant.DEFAULT_X);
                    map.getWce().setcY(Constant.DEFAULT_Y);
                    map.getWce().RE = WCE.E_MC;
                }
            }
        }
    }

    private boolean mcCanGetBackToBS(Map map, int nextChargingNode) {
        WCE mc = map.getWce();
        double RE_mc_i = mc.RE;
        double t_mc_i = map.distanceCalculate(Factor.WCE_INDEX, nextChargingNode) / WCE.V;
        double c = WCE.P_M;
        double distance_mc_i = map.distanceCalculate(Factor.WCE_INDEX, nextChargingNode);
        Sensor s = map.getSensor(nextChargingNode);
        double E = s.getE() - s.getP() * t_mc_i;
        RE_mc_i = RE_mc_i - c * distance_mc_i - (Sensor.E_MAX - E);
        double distance_i_bs = map.distanceCalculate(nextChargingNode, Factor.BS_INDEX);
        double E_i_bs = c * distance_i_bs;
        return RE_mc_i >= E_i_bs;
    }
}
class Message {
    private int ID;
    private double RE;
    private double t;
    private boolean urg;

    public Message(int id, double re, double t, boolean b) {
        ID = id;
        RE = re;
        this.t = t;
        urg = b;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getRE() {
        return RE;
    }

    public void setRE(double RE) {
        this.RE = RE;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public boolean isUrg() {
        return urg;
    }

    public void setUrg(boolean urg) {
        this.urg = urg;
    }
}
