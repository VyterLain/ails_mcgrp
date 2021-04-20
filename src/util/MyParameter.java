package util;

import structure.Data;

import java.util.Random;

public class MyParameter {

    public static Random random = new Random(1);
    public static double running_time = 3600;
    public static int N_ITER_PER_STAGE = 10;
    public static int k_max;
    public static double beta = 0.75;
    public static double gamma = 0.1;
    public static double tho_LS_full = 0.15;
    public static int ITER_BEFORE_KICK;

    public static void setRandomSeed(int seed){
        random = new Random(seed);
    }

    public static void setRunningTime(double time) { running_time = time; }

    public static void init(){
        k_max = Math.min(50, Data.total_requests - 2);
        ITER_BEFORE_KICK = 20000 * Math.max(1,
                20000 / (Data.total_requests * Data.total_requests
                        + Data.arcs
                        + 2 * Data.edges
                        + Data.nodes * Data.nodes / 5));
    }

}
