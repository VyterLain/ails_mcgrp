import structure.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Initialization {

    public Solution articleWay(Data data) {
        Solution s = augmentMerge(data);
        if (data.max_vehicles > 0 && s.routes.size() > data.max_vehicles) {
            s = binPacking(s);
//            s = binPacking(data);
        }
        if (s.routes.size() > s.data.max_vehicles) {
            System.out.println("oops, heuristic bin packing failed");
            s = gurobi_way(data);
        }
        return s;
    }

    private Solution gurobi_way(Data data) {
        // todo
        System.out.println("trying to use gurobi");
        return null;
    }

    public Solution augmentMerge(Data data) {
        List<Route> route_pool = new ArrayList<>();
        // step 1 初始化所有task单独形成一个路径
        for (Task t : data.tasks) {
            Route r = new Route(data);
            r.add(t);
            r.add(data.depot);
            route_pool.add(r);
        }
        // step 2 如果一个路径被另一个路径包含，那么将两条路径合并
        boolean has_domain = true;
        while (has_domain) {
            has_domain = false;
            for (int i = 0; i < route_pool.size(); i++) {
                int j = 0;
                Route r = route_pool.get(i);
                while (j < route_pool.size()) {
                    if (i == j) {
                        j++;
                        continue;
                    }
                    if (r.merge(route_pool.get(j))) {
                        has_domain = true;
                        route_pool.remove(j);
                        break;
                    } else j++;
                }
            }
        }
        // step 3,4 在满足容量约束的情况下将两条路径合并，并计算其成本节省量，选择最大值
        boolean has_merge = true;
        while (has_merge) {
            has_merge = false;
            int pre = -1;
            int successor = -1;
            int min_change = Integer.MAX_VALUE;
            for (int i = 0; i < route_pool.size(); i++) {
                Route r1 = route_pool.get(i);
                for (int j = 0; j < route_pool.size(); j++) {
                    if (i == j) continue;
                    Route r2 = route_pool.get(j);
                    if (r1.load + r2.load > data.max_capacity) continue;
                    int change = r1.if_connect(r2);
                    if (change < min_change) {
                        pre = i;
                        successor = j;
                        min_change = change;
                        has_merge = true;
                    }
                }
            }
            if (has_merge) {
                route_pool.get(pre).connect(route_pool.get(successor));
                route_pool.remove(successor);
            }
        }
        // done
        Solution s = new Solution(data);
        for (Route r : route_pool) s.add(r);
        return s;
    }

    public Solution binPacking(Solution s) {
        if (s.routes.size() <= s.data.max_vehicles) return s;
        boolean has_operation = true;
        while (has_operation) {
            has_operation = bin_packing_move(s);
            // if MOVE used, start from the beginning again
            if (!has_operation) {
                // SWAP
                has_operation = bin_packing_swap(s);
            }
            if (s.routes.size() <= s.data.max_vehicles) break;
        }
        s.getDist();
        return s;
    }

    private boolean bin_packing_swap(Solution s) {
        // SWAP(w_h > w_j && load_j — w_j > load_h — w_h)，交换两个bin中的task
        boolean has_changed = false;
        boolean has_operation = true;
        while (has_operation) {
            has_operation = false;
            // every w_h at bin_h
            for (int bin_h = 0; bin_h < s.routes.size(); bin_h++) {
                Route h = s.routes.get(bin_h);
                for (int cargo_h = 1; cargo_h < h.tasks.size() - 1; cargo_h++) {
                    Task wh = h.tasks.get(cargo_h);
                    // every w_j at bin_j
                    for (int bin_j = 0; bin_j < s.routes.size(); bin_j++) {
                        if (bin_h == bin_j) continue;
                        Route j = s.routes.get(bin_j);
                        for (int cargo_j = 1; cargo_j < j.tasks.size() - 1; cargo_j++) {
                            Task wj = j.tasks.get(cargo_j);
                            // if condition is true
                            if (wh.demand > wj.demand && j.load - wj.demand > h.load - wh.demand) {
                                if (j.load - wj.demand + wh.demand <= j.data.max_capacity) {
                                    Task tmp_h = h.remove(cargo_h);
                                    Task tmp_j = j.remove(cargo_j);
                                    h.greedyAdd(tmp_j);
                                    j.greedyAdd(tmp_h);
                                    has_operation = true;
                                    has_changed = true;
//                                    System.out.println("swap " + tmp_h + "@" + bin_h + " and " + tmp_j + "@" + bin_j);
                                }
                            }
                        }
                    }
                }
            }
        }
        return has_changed;
    }

    private boolean bin_packing_move(Solution s) {
        // MOVE，在两个bin中交换task，依据（1）满足约束；（2）不平衡指标增大(i->h, load_h > load_i — w_j),然后从头开始，直到上下界相同或者没有可行的操作
        boolean has_operation = false;
        // for every vehicle i
        int bin_i = 0;
        while (bin_i < s.routes.size()) {
            Route i = s.routes.get(bin_i);
            // for task j
            for (int cargo_j = 1; cargo_j < i.tasks.size() - 1; cargo_j++) {
                Task j = i.tasks.get(cargo_j);
                for (int bin_h = 0; bin_h < s.routes.size(); bin_h++) {
                    if (bin_h == bin_i) continue;
                    Route h = s.routes.get(bin_h);
                    if (h.load + j.demand <= h.data.max_capacity && h.load > i.load - j.demand) {
                        has_operation = true;
                        h.greedyAdd(i.remove(cargo_j));
//                            System.out.println("cargo " + j + "@bin " + bin_i + " to bin " + bin_h);
                        break;
                    }
                }
                if (has_operation) break;
            }
            if (has_operation) break;
            else bin_i++;
        }
        s.emptyRouteCheck();
        return has_operation;
    }

    public Solution binPacking(Data data) {
        // first fit
        Solution s = new Solution(data);
        for (Task t : data.tasks) {
            boolean has_added = false;
            for (Route r : s.routes) {
                if (t.demand + r.load <= data.max_capacity) {
                    r.greedyAdd(t);
                    has_added = true;
                }
            }
            if (!has_added) {
                Route r = new Route(data);
                r.add(t);
                r.add(data.depot);
                s.add(r);
            }
        }
        if (s.routes.size() > s.data.max_vehicles) binPacking(s);
        else s.getDist();
        return s;
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
