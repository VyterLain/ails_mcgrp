import structure.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Initialization {

    public Solution articleWay(Data data) {
        Solution s = augmentMerge(data);
        if (data.max_vehicles > 0 && s.routes.size() > data.max_vehicles)
            s = binPacking(data);
        return s;
    }

    /**
     * step 1: 初始化所有task单独形成一个路径
     * step 2: 如果一个路径被另一个路径包含，那么将两条路径合并
     * step 3: 在满足容量约束的情况下将两条路径合并，并计算其成本节省量，选择最大值
     * step 4: 重复第3步直到结束
     */
    public Solution augmentMerge(Data data) {
        List<Route> route_pool = new ArrayList<>();
        // step 1
        for (Task t : data.tasks) {
            Route r = new Route(data);
            r.add(t);
            r.add(data.depot);
            route_pool.add(r);
        }
        // step 2
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
        // step 3,4
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
        Solution s = new Solution(data);
        for (Route r : route_pool) s.add(r);
        return s;
    }

    /**
     * step 1: first fit构造初始解
     * step 2: 领域搜索（VNS），基于两个内部迭代过程
     *      step 2.1: MOVE，在两个bin中交换task，依据（1）满足约束；（2）不平衡指标增大(i->h, load_h > load_i — w_j)，
     *              然后从头开始，直到上下界相同或者没有可行的操作
     *      step 2.2: SWAP(w_h > w_j && load_h — w_h > load_j — w_j)，交换两个bin中的task
     * step 3: 不断执行（1）MOVE和（2）SWAP来重复搜索过程，直到上下界相同或者两个算子都没有可行的操作
     * step 4: 对于每个bin中的task使用贪婪方法插入
     */
    public Solution binPacking(Data data) {
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
