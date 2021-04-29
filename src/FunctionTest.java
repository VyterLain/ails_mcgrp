import structure.Data;
import structure.Route;
import structure.Solution;
import structure.Task;
import util.MyParameter;
import util.ReadData;
import util.destroy.*;
import util.local_search.*;
import util.repair.*;

import java.util.List;

public class FunctionTest {

    public static void main(String[] args) {
        try {
//            ReadData.get("src/data/cbmix/CBMix22.dat");
            Data data = ReadData.get("src/data/bhw/BHW6.dat");
            data.show();
            data.preprocess();
            MyParameter.init(data);

//            test_route_function(data);
//            test_destroy_repair(data);
//            test_ls_operators(data);
//            test_main_algorithm_structure(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test_running_efficiency(Data data) {
        // TODO
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
