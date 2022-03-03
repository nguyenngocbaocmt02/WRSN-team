package wrsn;

import java.util.ArrayList;
import java.util.Collections;

public class Population {
    private int N; // so luong ca the trong quan the
    private ArrayList<Individual> individuals; // Tap hop cac ca the
    public Population(int nIndividuals, Map map) {
        N = nIndividuals;
        individuals = new ArrayList<Individual>();
        for (int i = 0; i < N; i++) {
            // Khoi tao ca the i
            setIndividual(i, new Individual(map));
        }
        individuals.sort((i1, i2) -> Double.compare(i1.getFitnessF(), i2.getFitnessF()));
    }

    // Khoi tao quan the tai to hop tu quan the ban dau
    public Population(int nIndividuals, Population p) {
        N = nIndividuals;
        individuals = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            // Khoi tao ca the i
            setIndividual(i, p.getIndividual(i));
        }
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public ArrayList<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(ArrayList<Individual> individuals) {
        this.individuals = individuals;
    }

    public Individual getIndividual(int index) {
        return individuals.get(index);
    }

    public void setIndividual(int index, Individual individual) {
        if (index > individuals.size() - 1) individuals.add(individual);
        else individuals.set(index, individual);
    }
}
