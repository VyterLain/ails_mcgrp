package util;

import structure.*;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class ReadData {

    public static void get(String path) throws IOException {
        File file = new File(path);
        BufferedReader br;
        br = new BufferedReader(new FileReader(file));

        // basic info.
        Data.name = extract(br.readLine());
        br.readLine();
        Data.max_vehicles = Integer.parseInt(extract(br.readLine()));
        Data.max_capacity = Integer.parseInt(extract(br.readLine()));
        Data.depot_node = Integer.parseInt(extract(br.readLine()));
        Data.nodes = Integer.parseInt(extract(br.readLine()));
        Data.edges = Integer.parseInt(extract(br.readLine()));
        Data.arcs = Integer.parseInt(extract(br.readLine()));
        Data.nodes_r = Integer.parseInt(extract(br.readLine()));
        Data.edges_r = Integer.parseInt(extract(br.readLine()));
        Data.arcs_r = Integer.parseInt(extract(br.readLine()));

        Data.tasks = new Task[Data.nodes_r + Data.edges_r + Data.arcs_r];
        Data.edge_set = new HashMap<>();

        Data.graph = new Task[Data.nodes + 1][Data.nodes + 1];

        Data.raw_dist = new int[Data.nodes + 1][Data.nodes + 1];
        for (int[] a : Data.raw_dist)
            Arrays.fill(a, 10000);

        br.readLine();
        br.readLine();

        // required nodes
        for (int i = 0; i < Data.nodes_r; i++)
            Data.tasks[i] = extract(br.readLine(), TaskType.NODE, false);
        br.readLine();
        br.readLine();

        // required edges
        for (int i = 0; i < Data.edges_r; i++)
            Data.tasks[i + Data.nodes_r] = extract(br.readLine(), TaskType.EDGE, false);
        br.readLine();
        br.readLine();

        // edges
        for (int i = 0; i < Data.edges - Data.edges_r; i++)
            extract(br.readLine(), TaskType.EDGE, true);
        br.readLine();
        br.readLine();

        // required arcs
        for (int i = 0; i < Data.arcs_r; i++)
            Data.tasks[i + Data.nodes_r + Data.edges_r] = extract(br.readLine(), TaskType.ARC, false);
        br.readLine();
        br.readLine();

        // arcs
        for (int i = 0; i < Data.arcs - Data.arcs_r; i++)
            extract(br.readLine(), TaskType.ARC, true);

        br.close();
    }

    private static String extract(String line) {
        String[] lines = line.split("\t");
        return lines[lines.length - 1];
    }

    private static Task extract(String line, TaskType type, boolean deadhead) {
        String[] lines = line.split("\t");
        switch (type) {
            case NODE -> {
                int node = Integer.parseInt(lines[0].substring(1));
                int demand = Integer.parseInt(lines[1]);
                int cost = Integer.parseInt(lines[2]);
                NodeTask nt = new NodeTask(lines[0], demand, cost, node);
                Data.graph[node][node] = nt;
                return nt;
            }
            case EDGE -> {
                if (deadhead) {
                    int first = Integer.parseInt(lines[1]);
                    int second = Integer.parseInt(lines[2]);
                    int dist = Integer.parseInt(lines[3]);
                    Data.raw_dist[first][second] = Data.raw_dist[second][first] = dist;
                    Data.graph[first][second] = new Deadhead(lines[0], first, second, dist);
                    Data.graph[second][first] = new Deadhead(lines[0], second, first, dist);
                } else {
                    int first = Integer.parseInt(lines[1]);
                    int second = Integer.parseInt(lines[2]);
                    int dist = Integer.parseInt(lines[3]);
                    int demand = Integer.parseInt(lines[4]);
                    int cost = Integer.parseInt(lines[5]);
                    Data.raw_dist[first][second] = Data.raw_dist[second][first] = dist;
                    EdgeTask et = new EdgeTask(lines[0], demand, cost, first, second, dist);
                    EdgeTask et2 = new EdgeTask(lines[0], demand, cost, second, first, dist);
                    Data.graph[first][second] = et;
                    Data.graph[second][first] = et2;
                    Data.edge_set.put(et, et2);
                    Data.edge_set.put(et2, et);
                    return et;
                }
            }
            case ARC -> {
                if (deadhead) {
                    int head = Integer.parseInt(lines[1]);
                    int tail = Integer.parseInt(lines[2]);
                    int dist = Integer.parseInt(lines[3]);
                    Data.raw_dist[head][tail] = dist;
                    Data.graph[head][tail] = new Deadhead(lines[0], head, tail, dist);
                } else {
                    int head = Integer.parseInt(lines[1]);
                    int tail = Integer.parseInt(lines[2]);
                    int dist = Integer.parseInt(lines[3]);
                    int demand = Integer.parseInt(lines[4]);
                    int cost = Integer.parseInt(lines[5]);
                    Data.raw_dist[head][tail] = dist;
                    ArcTask at = new ArcTask(lines[0], demand, cost, head, tail, dist);
                    Data.graph[head][tail] = at;
                    return at;
                }
            }
        }
        return null;
    }

}
