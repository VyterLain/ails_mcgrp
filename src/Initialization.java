import structure.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Initialization {
    public Solution greedyWay() {
        Solution sol_init = new Solution();
        List<Task> ts = new ArrayList<>(Arrays.asList(Data.tasks));
        while (!ts.isEmpty()) {
            Route r = new Route();
            while (true) {
                int min_dist = Integer.MAX_VALUE;
                int min_type_index = -1;
                boolean reverse = false;
                int r_last_node = r.tasks.get(r.tasks.size() - 1).to;
                for (int i = 0; i < ts.size(); i++) {
                    Task t = ts.get(i);
                    if (t.demand + r.load <= Data.max_capacity) {
                        if (Data.dist[r_last_node][t.from] < min_dist) {
                            min_type_index = i;
                            min_dist = Data.dist[r_last_node][t.from];
                            if (t.type == TaskType.EDGE) reverse = false;
                        }
                        if (t.type == TaskType.EDGE) {
                            if (Data.dist[r_last_node][t.to] < min_dist) {
                                min_type_index = i;
                                min_dist = Data.dist[r_last_node][t.to];
                                reverse = true;
                            }
                        }
                    }
                }
                if (min_type_index < 0)
                    break;
                else {
                    Task _t = ts.remove(min_type_index);
                    if (reverse) r.add(Data.get_reverse_edge(_t));
                    else r.add(_t);
                }
            }
            r.add(Data.depot);
            sol_init.add(r);
        }
        return sol_init;
    }
}
