import structure.*;
import util.MyParameter;
import util.ReadData;
import util.destroy.*;
import util.local_search.*;
import util.repair.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FunctionTest {

    public static void main(String[] args) {
        try {
//            Data data = ReadData.get("src/data/cbmix/CBMix23.dat");
//            Data data = ReadData.get("src/data/bhw/BHW1.dat");
//            data.show();
//            data.preprocess();
//            MyParameter.init(data);

            Data[] data = test_read();
            for (Data d : data) {
//                test_initial(d);
//                test_segment(d);
//                test_route_function(d);
//                test_destroy_repair(d);
//                test_ls_operators(d);
                test_main_algorithm_structure(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test_initial(Data data) {
        Initialization init = new Initialization();
//            System.out.println("augment merge inst " + d.name);
//            System.out.println(init.augmentMerge(d));
//            System.out.println("bin packing inst " + d.name);
//            System.out.println(init.binPacking(d));
        System.out.println("article way inst " + data.name);
        Solution s = init.articleWay(data);
        if (!s.check_feasible()) System.out.println("false initial " + data.name);
        System.out.println(s);
    }

    private static void test_segment(Data data) {
        Segment s1 = new Segment(new int[]{6, 4, 3, 2, 1, 8, 9}, data);
        Segment s2 = new Segment(new int[]{3, 2, 1, 8}, data);
        Segment s3 = new Segment(new int[]{4, 3, 1, 9}, data);
        Segment s4 = new Segment(new int[]{1, 8, 9, 5}, data);
        Segment s5 = new Segment(new int[]{4}, data);
        Segment s6 = new Segment(new int[]{7, 4}, data);
        System.out.println(s6 + " connect " + s5 + " is " + s6.connect(s5));
        System.out.println(s1 + " connect " + data.tasks[3] + " is " + s1.connect(data.tasks[3]));
        System.out.println(s1 + " connect " + data.tasks[12] + " is " + s1.connect(data.tasks[12]));
    }

    private static void test_running_efficiency(Data data) {
        // TODO
    }

    private static Data[] test_read() throws IOException {
//        File file = new File("src/data/di-nearp/DI-NEARP-n699-Q4k.dat");
//        File file =new File("src/data/bhw/BHW1.dat");
//        File file = new File("src/data/mgval_50/mgval_0.50_9D.dat");
//        Data data = ReadData.get(file);
//        Data[] data = ReadData.getAll("src/data");
        Data[] data = ReadData.getAll("src/data/bhw/BHW6.dat");
//        Data[] data = ReadData.getAll("src/data/mggdb_45/mggdb_0.45_13.dat");
        for (Data d : data) {
//            d.show();
            System.out.println("start preprocessing " + d.name);
            d.preprocess();
        }
        return data;
    }

    private static void test_ls_operators(Data data) {
        Solution sol = new Initialization().greedyWay(data);
        Destructor destroy = new Random_Destructor();
        Constructor repair = new Random_Constructor();
        List<Task> removed = destroy.destruct(data, 23, sol);
        repair.construct(removed, sol);

        Operator opt = new Two_opt();

        sol.getDist();
        System.out.println(sol);

        System.out.println("local search...");

        opt.local_search(sol);
        sol.getDist();
        System.out.println(sol);

        System.out.println("solution feasibility => " + sol.check_feasible());

    }

    private static void test_main_algorithm_structure(Data data) {
        MyParameter.init(data);
        MyParameter.setRunningTime(5);
        Algorithm algo = new Algorithm();
        Solution sol = algo.run(data);
        sol.getDist();
        System.out.println(sol);
        System.out.println("solution feasibility => " + sol.check_feasible());

    }

    private static void test_route_function(Data data) {
        Solution sol = new Initialization().greedyWay(data);
        Route r = sol.routes.get(1);
        System.out.println(r);
        r.reverse_all_edge_tasks();
        System.out.println(r);
    }

    private static void test_destroy_repair(Data data) {

        double start = System.nanoTime();

        Solution sol = new Initialization().greedyWay(data);
        System.out.println(sol);

        Destructor destroy = new Edge_Destructor();
        List<Task> removed = destroy.destruct(data, 11, sol);
        sol.getDist();
        System.out.println(sol);

        System.out.println(removed);

        Constructor repair = new Greedy_Constructor();
        repair.construct(removed, sol);
        sol.getDist();
        System.out.println(sol);

        double end = System.nanoTime();
        System.out.println("total time cost => " + (end - start) / 1e9);

        System.out.println("solution feasibility => " + sol.check_feasible());

    }
}
