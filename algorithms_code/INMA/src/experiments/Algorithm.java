package experiments;

import utils.Constant;
import utils.Factor;
import utils.Factory;
import wrsn.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Algorithm {
    protected ArrayList<Integer> chargedNodes = new ArrayList<>();
    protected ArrayList<Integer> deadNodes = new ArrayList<>();
    protected double eMoveRatio = 0.0;
    protected double executedTime = 0;
    public double sumMove = 0;
    public double sumCharge = 0;
    protected int nodeCount = 0;
    protected int nextChargingNode = -1;  // sensor duoc sac tiep theo
    protected int chargingNode = -1;

    void execute(Map map, Factor factor) {
    }

    public ArrayList<Integer> getChargedNodes() {
        return chargedNodes;
    }

    public void setChargedNodes(ArrayList<Integer> chargedNodes) {
        this.chargedNodes = chargedNodes;
    }

    public ArrayList<Integer> getDeadNodes() {
        return deadNodes;
    }

    public void setDeadNodes(ArrayList<Integer> deadNodes) {
        this.deadNodes = deadNodes;
    }

    public double getExecutedTime() {
        return executedTime;
    }

    public void setExecutedTime(double executedTime) {
        this.executedTime = executedTime;
    }

    public double geteMoveRatio() {
        return eMoveRatio;
    }

    public void seteMoveRatio(double eMoveRatio) {
        this.eMoveRatio = eMoveRatio;
    }
}

// Yeu cau trong sac online
class Request {
    private int id; // dinh danh cua sensor
    private double RE; // nang luong con lai cuar sensor
    private double p; // muc tieu thu nang luong cua sensor
    private double ts; // thoi diem ghi nhan yeu cau

    public Request(int id, double RE, double p, double ts) {
        this.id = id;
        this.RE = RE;
        this.p = p;
        this.ts = ts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRE() {
        return RE;
    }

    public void setRE(double RE) {
        this.RE = RE;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getTs() {
        return ts;
    }

    public void setTs(double ts) {
        this.ts = ts;
    }
}

