import structure.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Initialization {

    // TODO: 增加原文中的初始化算法，包括bin packing和augment merge

    public Solution augmentMerge(){
        // TODO
        return null;
    }

    public Solution binPacking() {
        // TODO
        return null;
    }

    public Solution greedyWay(Data data) {
        Solution sol_init = new Solution(data);
        List<Task> ts = new ArrayList<>(Arrays.asList(data.tasks));
        while (!ts.isEmpty()) {
            Route r = new Route(data);
            while (true) {
                int min_dist = Integer.MAX_VALUE;
                int min_type_index = -1;
                boolean reverse = false;
                int r_last_node = r.tasks.get(r.tasks.size() - 1).to;
                for (int i = 0; i < ts.size(); i++) {
                    Task t = ts.get(i);
                    if (t.demand + r.load <= data.max_capacity) {
                        if (data.dist[r_last_node][t.from] < min_dist) {
                            min_type_index = i;
                            min_dist = data.dist[r_last_node][t.from];
                            if (t.type == TaskType.EDGE) reverse = false;
                        }
                        if (t.type == TaskType.EDGE) {
                            if (data.dist[r_last_node][t.to] < min_dist) {
                                min_type_index = i;
                                min_dist = data.dist[r_last_node][t.to];
                                reverse = true;
                            }
                        }
                    }
                }
                if (min_type_index < 0)
                    break;
                else {
                    Task _t = ts.remove(min_type_index);
                    if (reverse) r.add(data.get_reverse_edge(_t));
                    else r.add(_t);
                }
            }
            r.add(data.depot);
            sol_init.add(r);
        }
        return sol_init;
    }
}
