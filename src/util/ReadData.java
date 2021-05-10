package util;

import structure.*;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class ReadData {

    public static Data[] getAll(String dir_path) throws IOException {
        File dir = new File(dir_path);
        String[] files_name = dir.list();
        assert files_name != null;
        Data[] data = new Data[files_name.length];
        for (int i = 0; i < files_name.length; i++)
            data[i] = ReadData.get(dir_path + '/' + files_name[i]);
        return data;
    }

    public static Data get(String path) throws IOException {
        File file = new File(path);
        BufferedReader br;
        br = new BufferedReader(new FileReader(file));

        // basic info.
        Data data = new Data();
        data.name = extract(br.readLine());
        br.readLine();
        data.max_vehicles = Integer.parseInt(extract(br.readLine()));
        data.max_capacity = Integer.parseInt(extract(br.readLine()));
        data.depot_node = Integer.parseInt(extract(br.readLine()));
        data.nodes = Integer.parseInt(extract(br.readLine()));
        data.edges = Integer.parseInt(extract(br.readLine()));
        data.arcs = Integer.parseInt(extract(br.readLine()));
        data.nodes_r = Integer.parseInt(extract(br.readLine()));
        data.edges_r = Integer.parseInt(extract(br.readLine()));
        data.arcs_r = Integer.parseInt(extract(br.readLine()));

        data.tasks = new Task[data.nodes_r + data.edges_r + data.arcs_r];
        data.edge_set = new HashMap<>();

        data.graph = new Task[data.nodes + 1][data.nodes + 1];

        data.raw_dist = new int[data.nodes + 1][data.nodes + 1];
        for (int[] a : data.raw_dist)
            Arrays.fill(a, MyParameter.BIG_NUM);

        br.readLine();
        br.readLine();

        // required nodes
        for (int i = 0; i < data.nodes_r; i++)
            data.tasks[i] = extract(data, br.readLine(), TaskType.NODE, false);
        br.readLine();
        br.readLine();

        // required edges
        for (int i = 0; i < data.edges_r; i++)
            data.tasks[i + data.nodes_r] = extract(data, br.readLine(), TaskType.EDGE, false);
        br.readLine();
        br.readLine();

        // edges
        for (int i = 0; i < data.edges - data.edges_r; i++)
            extract(data, br.readLine(), TaskType.EDGE, true);
        br.readLine();
        br.readLine();

        // required arcs
        for (int i = 0; i < data.arcs_r; i++)
            data.tasks[i + data.nodes_r + data.edges_r] = extract(data, br.readLine(), TaskType.ARC, false);
        br.readLine();
        br.readLine();

        // arcs
        for (int i = 0; i < data.arcs - data.arcs_r; i++)
            extract(data, br.readLine(), TaskType.ARC, true);

        br.close();
        return data;
    }

    private static String extract(String line) {
        String[] lines = line.split("\t");
        return lines[lines.length - 1];
    }

    private static Task extract(Data data, String line, TaskType type, boolean deadhead) {
        String[] lines = line.split("\t");
        switch (type) {
            case NODE -> {
                int node = Integer.parseInt(lines[0].substring(1));
                int demand = Integer.parseInt(lines[1]);
                int cost = Integer.parseInt(lines[2]);
                NodeTask nt = new NodeTask(lines[0], demand, cost, node);
                data.graph[node][node] = nt;
                return nt;
            }
            case EDGE -> {
                if (deadhead) {
                    int first = Integer.parseInt(lines[1]);
                    int second = Integer.parseInt(lines[2]);
                    int dist = Integer.parseInt(lines[3]);
                    data.raw_dist[first][second] = data.raw_dist[second][first] = dist;
                    data.graph[first][second] = new Deadhead(lines[0], first, second, dist);
                    data.graph[second][first] = new Deadhead(lines[0], second, first, dist);
                } else {
                    int first = Integer.parseInt(lines[1]);
                    int second = Integer.parseInt(lines[2]);
                    int dist = Integer.parseInt(lines[3]);
                    int demand = Integer.parseInt(lines[4]);
                    int cost = Integer.parseInt(lines[5]);
                    data.raw_dist[first][second] = data.raw_dist[second][first] = dist;
                    EdgeTask et = new EdgeTask(lines[0], demand, cost, first, second, dist);
                    EdgeTask et2 = new EdgeTask(lines[0], demand, cost, second, first, dist);
                    data.graph[first][second] = et;
                    data.graph[second][first] = et2;
                    data.edge_set.put(et, et2);
                    data.edge_set.put(et2, et);
                    return et;
                }
            }
            case ARC -> {
                if (deadhead) {
                    int head = Integer.parseInt(lines[1]);
                    int tail = Integer.parseInt(lines[2]);
                    int dist = Integer.parseInt(lines[3]);
                    data.raw_dist[head][tail] = dist;
                    data.graph[head][tail] = new Deadhead(lines[0], head, tail, dist);
                } else {
                    int head = Integer.parseInt(lines[1]);
                    int tail = Integer.parseInt(lines[2]);
                    int dist = Integer.parseInt(lines[3]);
                    int demand = Integer.parseInt(lines[4]);
                    int cost = Integer.parseInt(lines[5]);
                    data.raw_dist[head][tail] = dist;
                    ArcTask at = new ArcTask(lines[0], demand, cost, head, tail, dist);
                    data.graph[head][tail] = at;
                    return at;
                }
            }
        }
        return null;
    }

}
