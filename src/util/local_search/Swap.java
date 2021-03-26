package util.local_search;

import structure.*;

public class Swap extends Operator {

    //  Swap exchanges the position of two tasks
    //  (both intra-route and inter-route).

    //  here, do not consider the directions of edges

    private int route_1;
    private int route_2;
    private int index_1;
    private int index_2;

    @Override
    public void local_search(Solution s) {
        while (get_best_move(s)) do_move(s);
    }

    @Override
    public boolean get_best_move(Solution s) {
        route_1 = -1;
        route_2 = -1;
        index_1 = -1;
        index_2 = -1;
        best_move_saving = Integer.MAX_VALUE;
        for (int i_1 = 0; i_1 < s.routes.size(); i_1++) {
            Route r1 = s.routes.get(i_1);
            for (int j_1 = 1; j_1 < r1.tasks.size() - 1; j_1++) {
                // for every task
                Task t1 = r1.tasks.get(j_1);
                for (int i_2 = i_1; i_2 < s.routes.size(); i_2++) {
                    Route r2 = s.routes.get(i_2);
                    for (int j_2 = 1; j_2 < r2.tasks.size() - 1; j_2++) {
                        if (i_1 == i_2 && j_2 <= j_1) continue;
                        // for every task,
                        // except the one has been visited
                        Task t2 = r2.tasks.get(j_2);
                        if (r1.load - t1.demand + t2.demand > Data.max_capacity
                                || r2.load - t2.demand + t1.demand > Data.max_capacity) continue;
                        int change = 0;
                        // get the pre and next of t1
                        int pre_node_1 = r1.tasks.get(j_1 - 1).to;
                        int nex_node_1 = r1.tasks.get(j_1 + 1).from;
                        // get the pre and next of t2
                        int pre_node_2 = r2.tasks.get(j_2 - 1).to;
                        int nex_node_2 = r2.tasks.get(j_2 + 1).from;
                        if (i_1 == i_2 && j_2 == j_1 + 1) change = getChangeForAdjacentTask(t1, t2, change, pre_node_1, nex_node_2);
                        else {
                            // change - remove dist
                            // change + add dist
                            change = getChangeForNotAdjacentTask(t1, change, pre_node_1, nex_node_1, pre_node_2, nex_node_2);
                            change = getChangeForNotAdjacentTask(t2, change, pre_node_2, nex_node_2, pre_node_1, nex_node_1);
                        }
                        // if we find a better move, replace current move
                        if (change < 0 && change < best_move_saving) {
                            route_1 = i_1;
                            route_2 = i_2;
                            index_1 = j_1;
                            index_2 = j_2;
                            best_move_saving = change;
                        }
                    }
                }
            }
        }
        // if route_1 < 0, it means we cannot find a valid move
        return route_1 >= 0;
    }

    private int getChangeForAdjacentTask(Task t1, Task t2, int change, int pre, int next) {
        change -= (Data.dist[pre][t1.from] + Data.dist[t1.to][t2.from] + Data.dist[t2.to][next]);
        change += (Data.dist[pre][t2.from] + Data.dist[t2.to][t1.from] + Data.dist[t1.to][next]);
        return change;
    }

    private int getChangeForNotAdjacentTask(Task t, int change,
                                            int pre_node_remove, int nex_node_remove,
                                            int pre_node_insert, int nex_node_insert) {
        change -= (Data.dist[pre_node_remove][t.from] + Data.dist[t.to][nex_node_remove]);
        change += (Data.dist[pre_node_insert][t.from] + Data.dist[t.to][nex_node_insert]);
        return change;
    }

    @Override
    public void do_move(Solution s) {
        assert route_1 != route_2 || index_2 > index_1;
        Task t2 = s.routes.get(route_2).remove(index_2);
        // removing t2 will not affect removing t1 ( t2 > t1 )
        Task t1 = s.routes.get(route_1).remove(index_1);
        s.routes.get(route_1).add(t2, index_1);
        s.routes.get(route_2).add(t1, index_2);
//        System.out.println("change " + t2 + " and " + t1 + " saving => " + best_move_saving);
    }
}
