package utils;

public class Factor {
    public static double P_MULTIPLIER = 1;
    public static double P_MULTIPLIER_1 = 1;
    public static double P_MULTIPLIER_2 = 1;
    public static double P_BOUND = 0;
    // Tham so chu ki sac
    public static double T = 200000;   //s, thoi gian trien khai mang
    public static double t = 0;   //s, thoi gian xet mang
    public static final double T_INTERVAL = 10; //s, khoang thoi gian gui thong diep voi cac thuat toan online
    public static final int BS_INDEX = -1;
    public static final int WCE_INDEX = -2;
    public static int M = 1000;
    public static double P_MIN = 1e-5;

    // Trang thai xe sac WCE
    public static final int IDLE = 1;
    public static final int MOVING = 2;
    public static final int CHARGING = 3;
    public static final int REPLENISH = 4;

    // Tham so thuat toan
    public static final double STOP_EVAL = 1e5; // so luong danh gia toi da
    public static final double STOP_FITNESS = 0.0;  // fitness toi uu
    public static final int SEEDS = 10; // So luong seed
    public static int SEED = 0; // SEED hien tai
    public static int N = 200; // So luong ca the trong quan the mac dinh

    // Tham so thuat toan GACS
    // phase 1
    public static final double CROSSOVER_RATE_1 = 0.75;
    public static final double PMX_RATE = 0.5;
    public static final double SPX_RATE = 0.5;
    public static final double MUTATION_RATE_1 = 0.1;
    public static final double SWAP_RATE = 0.5;
    public static final double CIM_RATE = 0.5;

    //  Thma so thuat toan PA
    public static final double alpha = 0.6;


    // Tham so fitness f
    public static final double ALPHA = 0.5;

//    For experiment 20/08/2020
    public static final double U_DEFAULT = 4;
    public static final int E_MC_DEFAULT = 108000;
    public static final int V_DEFAULT = 5;
    public static final int T_DEFAULT = 200000;
    public static final double P_DEFAULT = 1;


//    public static final int[] scenario_factor = {1,2,3,4,5,6,7};
    public static final int[] scenario_factor = {1,2,7};
    public static final String[] algorithm = {"hgacmaes", "samer", "gacs", "fcfs", "pa\\" + alpha, "inma", "nah"};
//    public static final int[] Us = {2};
//    public static final int[] Us = {5, 10, 15, 20, 25};
    public static final double[] Us = {4.0/3.0, 4.0 * 2 / 3.0, 4, 4.0 * 4 / 3.0, 4.0 * 5 / 3.0};
    public static final int[] E_MC_1s = {54000, 108000, 162000, 216000, 270000};
    public static final int[] E_MC_2s = {108000, 216000, 324000, 432000, 540000};
    public static final int[] Vs = {2, 5, 7, 10, 15, 20};
    public static final int[] Ts = {100000, 200000, 300000, 400000, 500000};
//    public static final double[] Ps = {1, 2, 3, 4, 5};
//    public static final double[] Ps = {1, 1.5, 2, 2.5, 3};
//        public static final double[] Ps = {0.8, 1, 1.2, 1.4, 1.6, 1.8, 2};
//public static final double[] Ps = {1, 1.5};
 public static final double[] Ps = {0.5, 1 , 1.5, 2.0, 2.5};
}
