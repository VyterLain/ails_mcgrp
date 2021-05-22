import structure.*;
import util.MyParameter;
import util.ReadData;
import util.WriteData;
import util.destroy.*;
import util.local_search.*;
import util.repair.*;

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
        if (!s.check_feasible()) System.out.println("\t\tfalse initial " + data.name);
//        System.out.println(s);
    }

    private static void test_running_efficiency(Data data) {
        // TODO
    }

    private static Data[] test_read() throws IOException {
//        Data[] data = ReadData.getAll("src/data/mgval");
        Data[] data = ReadData.getAll("src/data/bhw/BHW6.dat");
//        Data[] data = ReadData.getAll("src/data/mggdb_45/mggdb_0.45_13.dat");
        for (Data d : data) {
//            d.show();
//            System.out.println("start preprocessing " + d.name);
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

    private static void test_main_algorithm_structure(Data data) throws IOException {
        System.out.println("******* starting ls...");
        MyParameter.init(data);
        MyParameter.setRunningTime(3600);
        MyParameter.setRandomSeed(1);
        Algorithm algo = new Algorithm();
        Solution sol = algo.run(data);
        sol.getDist();
//        System.out.println(sol);
//        System.out.println("solution feasibility => " + sol.check_feasible());
        WriteData.write(sol, data);
        System.out.println(data.name + " done");
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
