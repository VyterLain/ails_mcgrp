package structure;

import java.util.Map;

public class Data {

    // TODO: 增加原文中的初始化算法，在Data类中，添加depot到task的路径列表，可能实现：哈希表

    public static String name;
    public static int max_vehicles;
    public static int max_capacity;

    public static int nodes;
    public static int arcs;
    public static int edges;

    public static int nodes_r;
    public static int arcs_r;
    public static int edges_r;
    public static int total_requests;

    public static int depot_node;
    public static Task depot;
    public static Task[] tasks;
    public static Map<Task, Task> edge_set;

    public static int[][] raw_dist;
    public static int[][] dist; // use Dijkstra algorithm

    // graph[i][j] means task t( from i to j ) else null
    public static Task[][] graph;

    // minimum traversal cost between r and t, for related_destructor
    public static int max_min_travel_r_t;
    // the demand of task t
    public static int max_demand_t;

    public static void preprocess() {
        dist = new int[nodes + 1][nodes + 1];
        for (int i = 0; i < nodes + 1; i++) {
            for (int j = 0; j < nodes + 1; j++)
                if (i != j) dist[i][j] = raw_dist[i][j];
        }

        // Dijkstra algorithm, o(n^2)
        for (int node = 1; node <= nodes; node++) {
            // for one row
            boolean[] visited = new boolean[nodes + 1];
            int[] row_dist = dist[node];
            for (int i = 1; i <= nodes; i++) {
                // find min dist node
                int min_d = Integer.MAX_VALUE;
                int min_n = -1;
                for (int j = 1; j <= nodes; j++) {
                    if (!visited[j] && min_d >= row_dist[j]) {
                        min_d = row_dist[j];
                        min_n = j;
                    }
                }
                // mark
                visited[min_n] = true;
                // update other node
                for (int j = 1; j <= nodes; j++)
                    if (!visited[j] && row_dist[j] > row_dist[min_n] + raw_dist[min_n][j])
                        row_dist[j] = row_dist[min_n] + raw_dist[min_n][j];
            }
        }

        // other preprocess
        total_requests = nodes_r + arcs_r + edges_r;
        depot = new NodeTask("N" + depot_node, 0, 0, depot_node);
        max_min_travel_r_t = get_max_min_travel_r_t(); // o(n)
        max_demand_t = get_max_demand(); // o(n)
    }

    // for test
    public static void show() {
        String s = name + "=>" + "\n\t" +
                "max v=" + max_vehicles + "\n\t" +
                "max c=" + max_capacity + "\n\t" +
                "depot=" + depot_node + "\n\t" +
                "all n=" + nodes + "\n\t" +
                "all e=" + edges + "\n\t" +
                "all a=" + arcs + "\n\t" +
                "req n=" + nodes_r + "\n\t" +
                "req e=" + edges_r + "\n\t" +
                "req a=" + arcs_r;
        System.out.println(s);
    }

    public static Task get_reverse_edge(Task t) { return edge_set.getOrDefault(t, t); }

    private static int get_max_min_travel_r_t() {
        int res = 0;
        for (Task t : tasks) {
            if (t.type == TaskType.EDGE) {
                for (Task r : tasks) {
                    if (res < dist[t.to][r.from]) res = dist[t.to][r.from];
                    if (res < dist[t.from][r.from]) res = dist[t.from][r.from];
                    if (r.type == TaskType.EDGE) {
                        if (res < dist[t.to][r.to]) res = dist[t.to][r.to];
                        if (res < dist[t.from][r.to]) res = dist[t.from][r.to];
                    }
                }
            } else {
                for (Task r : tasks) {
                    if (res < dist[t.to][r.from]) res = dist[t.to][r.from];
                    if (r.type == TaskType.EDGE) {
                        if (res < dist[t.to][r.to]) res = dist[t.to][r.to];
                    }
                }
            }
        }
        return res;
    }

    private static int get_max_demand() {
        int res = 0;
        for (Task t : tasks) {
            if (res < t.demand) res = t.demand;
        }
        return res;
    }
}
