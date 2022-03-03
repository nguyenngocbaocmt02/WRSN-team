package experiments;

import utils.Factor;
import wrsn.Individual;
import wrsn.Map;
import wrsn.Population;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GACS extends Algorithm {
    @Override
    public void execute(Map map, Factor factor) {
        // Khoi tao quan the ban dau
        Population P = new Population(Factor.N, map);
        double timestamp = 0;
        while (timestamp < Factor.T) {
            phase1(P, map, factor);
            phase2(P, map, factor);
            timestamp ++;
        }
    }

    private void phase1(Population P, Map map, Factor factor) {
        int populationSize = P.getN();  // kich thuoc quan the
        int N = map.getN(); // so luong sensor
        // Khoi tao va tinh fitness
        for (Individual individual : P.getIndividuals()) {
            individual.calculateFitnessFGACS(map);
        }
        // Tien hanh lai ghep va dot bien
        ArrayList<Individual> offspingIndividuals = new ArrayList<>();
        Random rand= new Random();
        rand.setSeed(Factor.SEED);
        for (int i = 0; i < N; i++) {
            double rmx = rand.nextDouble();
            // xac suat de lai ghep
            if (rmx < Factor.CROSSOVER_RATE_1) {
                int parent1 = rand.nextInt(N);
                int parent2 = rand.nextInt(N);
                double rmpmx = rand.nextDouble();
                if (rmpmx < Factor.PMX_RATE) {
                    // lai ghep pmx
                    offspingIndividuals.addAll(PMX(P.getIndividual(parent1), P.getIndividual(parent2)));
                }
                else {
                    // lai ghep spx
                    offspingIndividuals.addAll(SPX(P.getIndividual(parent1), P.getIndividual(parent2)));
                }
            }
        }
    }

    private ArrayList<Individual> PMX(Individual parent1, Individual parent2) {
        int N = parent1.getN();
        Random rand= new Random();
        rand.setSeed(Factor.SEED);
        int point1 = rand.nextInt(N); // chon ngau nhien diem cat thu nhat
        int point2 = rand.nextInt(N); // chon ngau nhien diem cat thu hai
        Individual offspring1 = new Individual(parent1);
        Individual offspring2 = new Individual(parent2);
        
        return new ArrayList<>() {{
            add(offspring1);
            add(offspring2);
        }};
    }

    private ArrayList<Individual> SPX(Individual parent1, Individual parent2) {
        int N = parent1.getN();
        Random rand= new Random();
        rand.setSeed(Factor.SEED);
        int point = rand.nextInt(N); // chon ngau nhien mot diem cat
        Individual offspring1 = new Individual(parent1);
        Individual offspring2 = new Individual(parent2);
        int[] location1 = new int[N - point]; // vi tri cua cac sensor ben phai diem cat con 1
        int[] location2 = new int[N - point]; // vi tri cua cac sensor ben phai diem cat con 2
        int pointSize = N - point; //kich thuoc ben phai diem cat cua cac con
        int loc1Count = 0;
        int loc2Count = 0;

        // Xac dinh thu tu ben phai diem cat cua con 1 tren cha me 2
        for (int i = 0; i < N; i++) {
            if (loc1Count == pointSize) break;
            for (int j = point; j < N; j++) {
                if (parent1.getNode(i) == offspring1.getNode(j)) {
                    location1[loc1Count] = parent1.getNode(i);
                    loc1Count ++;
                }
            }
        }

        // Xac dinh thu tu ben phai diem cat cua con 2 tren cha me 1
        for (int i = 0; i < N; i++) {
            if (loc2Count == pointSize) break;
            for (int j = point; j < N; j++) {
                if (parent2.getNode(i) == offspring2.getNode(j)) {
                    location2[loc2Count] = parent2.getNode(i);
                    loc1Count ++;
                }
            }
        }

        for (int i = point; i < N; i++) {
            offspring1.setNode(i, location1[i - point]);
            offspring2.setNode(i, location2[i - point]);
        }

        return new ArrayList<>() {{
            add(offspring1);
            add(offspring2);
        }};
    }

    private void phase2(Population P, Map map, Factor factor) {
    }
}
