package wrsn;

import utils.Factor;
import utils.Factory;

import java.util.ArrayList;
import java.util.Collections;

public class Individual {
    private int N; // so luong gene
    private ArrayList<Integer> path; // duong di cua xe sac qua cac sensor

    private double totalDistance;   // tong chi phi duong di
    private ArrayList<Double> taus; // thoi gian sac cua cac sensor
    private double fitnessF;    // fitness f - danh gia duong di
    private double fitnessG;    // fitness g - danh gia thoi gian sac

    public Individual(Map map) {
        N = map.getN();
        path = new ArrayList<>();
        taus = new ArrayList<>();
        // Chi gan gia tri cho duong di truoc
        for (int i = 0; i < N; i++) {
            setNode(i, i);
        }
        // Sinh hoan vi
        Collections.shuffle(path);
    }

    public Individual(Individual individual) {
        N = individual.getN();
        path = new ArrayList<>(individual.getPath());
        taus = new ArrayList<>(individual.getTaus());
    }

    private void calculateTotalDistance(Map map) {
        int nSensors = path.size();
        for (int i = 0; i < nSensors; i++) {
            int previous = i == 0 ? Factor.BS_INDEX : path.get(i - 1);
            int current = path.get(i);
            totalDistance += map.distanceCalculate(previous, current);
        }
        totalDistance += map.distanceCalculate(path.get(nSensors - 1), Factor.BS_INDEX);
    }
    public void calculateFitnessFGACS(Map map) {
        Factory factory = new Factory();
        fitnessF  = factory.fitnessFGACS(path, map);
    }
    private void calculateFitnessF(Map map) {
        Factory factory = new Factory();
        fitnessF  = factory.fitnessF(path, map);
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public int getN() {
        return N;
    }

    public double getFitnessF() {
        return fitnessF;
    }

    public double getFitnessG() {
        return fitnessG;
    }

    public ArrayList<Integer> getPath() {
        return path;
    }

    public void getPath(ArrayList<Integer> path) {
        this.path = path;
    }

    public int getNode(int index) {
        return path.get(index);
    }

    public void setNode(int index, int node) {
        if (index > path.size() - 1) path.add(node);
        else path.set(index, node);
    }

    public ArrayList<Double> getTaus() {
        return taus;
    }

    public void setTaus(ArrayList<Double> taus) {
        this.taus = taus;
    }

    public Double getTau(int index) {
        return taus.get(index);
    }

    public void setTau(int index, Double tau) {
        if (index > taus.size() - 1) taus.add(tau);
        else taus.set(index, tau);
    }
}
