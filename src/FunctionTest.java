import structure.*;
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

            test_initial(data);
            test_segment(data);
//            test_route_function(data);
//            test_destroy_repair(data);
//            test_ls_operators(data);
//            test_main_algorithm_structure(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test_initial(Data data) {
        // TODO
    }

    private static void test_segment(Data data) {
        Segment s1 = new Segment(new int[]{6, 4, 3, 2, 1, 8, 9}, 100, data);
        Segment s2 = new Segment(new int[]{3, 2, 1, 8}, 50, data);
        Segment s3 = new Segment(new int[]{4, 3, 0, 7}, 50, data);
        Segment s4 = new Segment(new int[]{1, 8, 9, 5}, 50, data);
        Segment s5 = new Segment(new int[]{4}, 5, data);
        System.out.println(s1 + " domain " + s2 + " is " + s1.domain(s2));
        System.out.println(s1 + " domain " + s3 + " is " + s1.domain(s3));
        System.out.println(s1 + " domain " + s4 + " is " + s1.domain(s4));
        System.out.println(s1 + " domain " + s5 + " is " + s1.domain(s5));
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
