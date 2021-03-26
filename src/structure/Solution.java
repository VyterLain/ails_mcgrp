package structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Solution {

    public int dist;

    public List<Route> routes = new ArrayList<>();

    public Solution() {
        this.dist = 0;
    }

    public Solution(Solution sol) {
        this.dist = sol.dist;
        for (Route r : sol.routes) {
            this.routes.add(new Route(r));
        }
    }

    public void add(Route r) {
        this.dist += r.dist;
        this.routes.add(r);
    }

    public Route remove(int index) {
        this.dist -= this.routes.get(index).dist;
        return this.routes.remove(index);
    }

    public void emptyRouteCheck() {
        routes.removeIf(route -> route.tasks.size() == 2);
    }

    public boolean check_feasible() {
        HashSet<Task> task_set = new HashSet<>(Arrays.asList(Data.tasks.clone()));

        for (Route r : routes) {
            int r_load = 0;
            for (Task t : r.tasks) {
                // skip DEPOT
                if (t.equals(Data.depot)) continue;
                r_load += t.demand;
                if (t.type==TaskType.EDGE) {
                    if (!task_set.contains(t) && !task_set.contains(Data.get_reverse_edge(t))) return false;
                }else
                    if (!task_set.contains(t)) return false;
                task_set.remove(t);
                task_set.remove(Data.get_reverse_edge(t));
            }
            if (r_load > Data.max_capacity) return false;
            if (!(r.tasks.get(0).equals(Data.depot)
                    && r.tasks.get(r.tasks.size() - 1).equals(Data.depot))) return false;
        }

        return task_set.isEmpty();
    }

    public int getDist() {
        this.dist = 0;
        for (Route r : routes) {
            this.dist += r.dist;
        }
        return this.dist;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Solution{cost=").append(dist).append(", solution=\n");
        for (Route r : routes) {
            sb.append(r.toString()).append("\n");
        }
        sb.append('}');

        return sb.toString();
    }
}
