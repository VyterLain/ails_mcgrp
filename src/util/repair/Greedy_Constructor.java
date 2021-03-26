package util.repair;

import structure.*;

import java.util.List;
import java.util.Set;

public class Greedy_Constructor extends Constructor {

    //  In each iteration, the task
    //  with the minimal best insertion cost
    //  is inserted in its best position.

    private int best_insert_route;
    private int best_insert_r_index;
    private Task best_task;

    @Override
    public void construct(List<Task> remains, Solution sol) {
        Set<Task> set = list2set(remains);
        while (!set.isEmpty()) {
            if (find_best_insertion(set, sol)) {
                sol.routes.get(best_insert_route).add(best_task, best_insert_r_index);
                set.remove(best_task);
                if (best_task.type == TaskType.EDGE) set.remove(Data.get_reverse_edge(best_task));
                continue;
            }
            Route r = new Route();
            r.add(Data.depot);
            sol.add(r);
        }
    }

    private boolean find_best_insertion(Set<Task> set, Solution s) {
        best_insert_route = -1;
        best_insert_r_index = -1;
        best_task = null;
        int best_insertion_cost = Integer.MAX_VALUE;
        // travel all removed tasks
        for (Task t : set) {
            // travel all routes
            for (int j = 0; j < s.routes.size(); j++) {
                Route r = s.routes.get(j);
                if (r.load + t.demand > Data.max_capacity)
                    continue;
                // travel all position in a route
                for (int k = 1; k < r.tasks.size(); k++) {
                    int pre_node = r.tasks.get(k - 1).to;
                    int nex_node = r.tasks.get(k).from;
                    // consider three type of task t
                    int change = t.dist - Data.dist[pre_node][nex_node] + (Data.dist[pre_node][t.from] + Data.dist[t.to][nex_node]);
                    if (change < best_insertion_cost) {
                        best_insert_route = j;
                        best_insert_r_index = k;
                        best_insertion_cost = change;
                        best_task = t;
                    }
                }
            }
        }
        return best_insert_route >= 0;
    }
}
