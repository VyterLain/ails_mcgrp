import structure.Data;
import structure.Route;
import structure.Solution;
import structure.Task;
import util.MyParameter;
import util.ReadData;
import util.destroy.*;
import util.local_search.*;
import util.repair.*;

import java.util.ArrayList;
import java.util.List;

public class FunctionTest {

    public static void main(String[] args) {
        try {
            ReadData.get("src/data/cbmix/CBMix22.dat");
//            ReadData.get("src/data/bhw/BHW5.dat");
            Data.show();
            Data.preprocess();
            MyParameter.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        test_route_function();
//        test_destroy_repair();
//        test_ls_operators();
        test_main_algorithm_structure();
    }

    private static void test_ls_operators() {
        Solution sol = new Initialization().greedyWay();
        Destructor destroy = new Random_Destructor();
        Constructor repair = new Random_Constructor();
        List<Task> removed = destroy.destruct(23, sol);
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

    private static void test_main_algorithm_structure() {

        Algorithm algo = new Algorithm();
        Solution sol = algo.run();
        sol.getDist();
        System.out.println(sol);
        System.out.println("solution feasibility => " + sol.check_feasible());

    }

    private static void test_route_function() {
        Solution sol = new Initialization().greedyWay();
        Route r = sol.routes.get(1);
        System.out.println(r);
        r.reverse_all_edge_tasks();
        System.out.println(r);
    }

    private static void test_destroy_repair() {

        double start = System.nanoTime();

        Solution sol = new Initialization().greedyWay();
        System.out.println(sol);

        Destructor destroy = new Edge_Destructor();
        List<Task> removed = destroy.destruct(11, sol);
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
