package experiments;

import utils.Factor;
import utils.Factory;
import wrsn.Individual;
import wrsn.Map;
import wrsn.Population;

import java.util.Arrays;
import java.util.Random;

// A Hybrid Genetic Algorithm and Covariance Matrix Adaptation Evolution Strategy Algorithm
public class HGACMAES extends Algorithm {

    @Override
    public void execute(Map map, Factor factor) {
        // Khoi tao quan the ban dau
        Population P = new Population(Factor.N, map);
        // Khoi tao quan the tai to hop
        Population K = new Population(Factor.N, P);
        int t = 0;
        while (t < Factor.STOP_EVAL) {
            K = new Population(Factor.N / 2, K);
            CMAES(K, map);
            t ++;
        }
    }

    private void CMAES(Population K, Map map) {
// Mot so tham so dau vao
        int N = map.getN();   // so chieu bai toan
        double[] xmean = new double[N]; // x trung binh
        for (int i = 0; i < N; i++) {
            xmean[i] = new Random().nextDouble();
        }
        double sigma = 0.3; // do lech chuan
        double stopFitness = 1e-10;
        double stopEval = 1e3*N*N;

// Các tham so chon loc
        int lambda = K.getN();   // kich thuoc quan the
        double mu = lambda / 2;     // so luong cha me dung de tai to hop
        int mu_int = (int) Math.floor(mu); // lam tron xuong voi mu
        double[] weights = new double[mu_int]; // trong so tai to hop
        double sumWeights = 0;  // tong trong so tai to hop
        double sumSqWeights = 0; // tong trong so tai to hop binh phuong
        // weights = log(mu+1/2)-log(1mu)';
        for (int i = 0; i < mu_int; i++) {
            weights[i] = Math.log(mu + 0.5) - Math.log(i + 1);
            sumWeights += weights[i];
        }
        // Chuan hoa trong so Tong trong so = 1
        for (int i = 0; i < mu_int; i++) {
            weights[i] = weights[i] / sumWeights;
            sumSqWeights += weights[i] * weights[i];
        }
        sumWeights = Arrays.stream(weights).sum();
        /* variance effectiveness
            mueff=sum(weights)^2/sum(weights.^2);
         */
        double mueff = sumWeights * sumSqWeights / sumSqWeights;

// Cac tham so thich ung
        double cc = (4 + mueff / N) / (N + 4 + 2 * mueff / N);
        double cs = (mueff + 2) / (N + mueff + 5);
        double c1 = 2 / ((N + 1.3)*(N + 1.3) + mueff);
        double cmu = Math.min(1 - c1, 2 * (mueff - 2 + 1 / mueff) / ((N + 2)*(N + 2) + mueff));
        double damps = 1 + 2 * Math.max(0, Math.sqrt((mueff - 1)/(N + 1)) - 1) + cs;

// Cac tham so khoi tao va hang so
        double[] pc = new double[N]; // duong tien hoa cua ma tran hiep phuong sai C
        double[] ps = new double[N]; // duong tien hoa cho do lech chuan sigma
        double[][] B = new double[N][N];    // he truc toa do B
        // Khoi tao la ma tran don vi
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == j) B[i][j] = 1;
            }
        }
        double[] D = new double[N]; // duong cheo ma tran D
        for (int i = 0; i < N; i++) {
            D[i] = 1;
        }
        double[][] C = new double[N][N];    // ma trna hiep phuong sai C
        double[][] invsqrtC = new double[N][N]; // ma tran nghich dao can bac hai C
        calculateOrthonormalMartix(N, C, B, D, 2);
        calculateOrthonormalMartix(N, C, B, D, -1);
        int eigenval = 0;
        double chiN = Math.sqrt(N) * (1 - 1 / (4 * N) + 1 / (21 * N * N));

        /*
        % -------------------- Generation Loop --------------------------------
         */
        int counteval = 0;
        while (counteval < Factor.STOP_EVAL) {
            // Sinh va danh gia lambda ca the con
            double[][] arx = new double[lambda][N];
            double[] arfitness = new double[N];
            for (int k = 0; k < lambda; k++) {
                arx[k] = generateX(xmean, sigma, B, D);    // m + sig * Normal(0,C)
                arfitness[k] = feval(K.getIndividual(k), arx[k], map);
            }
            counteval ++;
        }
    }

    private double feval(Individual i, double[] arx, Map map) {
        Factory factory = new Factory();
        return factory.fitnessG(i, arx, map);
    }

    private double[] generateX(double[] xmean, double sigma, double[][] B, double[] D) {
        int N = xmean.length;
        double[] x = new double[N]; // ca the sinh ra
        double[] factorX = new double[N];   // nhan tu trong qua trinh sinh
        /*
        x = xmean + sigma * B * (D .* randn(N,1));
         */
        // factorX = D .* randn(N,1);
        for (int i = 0; i < N; i++) {
            factorX[i] = D[i] * new Random().nextGaussian();
        }
        // x = xmean + sigma * B * factorX;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                x[i] += B[i][j] * factorX[j];
            }
            x[i] *= sigma;
            x[i] += xmean[i];
        }
        return x;
    }

    private void calculateOrthonormalMartix(int N, double[][] C, double[][] B, double[] D, int exponent) {
        double[][] tranposeB = new double[N][N];    // B chuyen vi
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tranposeB[i][j] = B[j][i];
            }
        }
        double[][] diagPowerD = new double[N][N];  // ma tran duong cheo D luy thua power tuong ung
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == j) diagPowerD[i][j] = Math.pow(D[i], exponent);
            }
        }
        // C = B * diag(D.^2) * B';
        // C = B * diag(D.^2);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == j) C[i][j] = B[i][j] * diagPowerD[i][j];
            }
        }
        // C = C * B';
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == j) C[i][j] = C[i][j] * tranposeB[i][j];
            }
        }

    }
}
