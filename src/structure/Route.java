package structure;

import java.util.ArrayList;
import java.util.List;

public class Route {

    // 1."add" function will consider that
    //   whether the edge is reverse
    // 2."remove" function only check the edge
    //   by the next_nodes list
    // 3.every time we use "add" or "remove",
    //   the next_nodes list will update
    // 4.the next_nodes list is fixed for a typical route
    //   but the edge in the tasks list not
    // 5."refresh" function can help to
    //   verify and modify the edge
    //   by the next_nodes list

    public int dist = 0;
    public int load = 0;

    public int nodes = 0;
    public int edges = 0;
    public int arcs = 0;

    public List<Task> tasks = new ArrayList<>();

    public Route() {
        nodes++;
        this.tasks.add(Data.depot);
    }

    public Route(Route route) {
        this.dist = route.dist;
        this.load = route.load;
        this.nodes = route.nodes;
        this.edges = route.edges;
        this.arcs = route.arcs;
        this.tasks = new ArrayList<>(route.tasks);
    }

    public void add(Task t) {
        int last_node = tasks.get(tasks.size() - 1).to;
        tasks.add(t);
        load += t.demand;
        dist += (Data.dist[last_node][t.from] + t.dist);
        switch (t.type) {
            case NODE -> nodes++;
            case EDGE -> edges++;
            case ARC -> arcs++;
        }
    }

    public void add(Task t, int index) {

        // cannot add before depot or out of bounds
        if (index <= 0 || index >= tasks.size()) {
            System.out.println("add(index wrong) in route\n");
            System.exit(1);
        }

        // pre_node(pre_task), task(from_node, to_node), nex_node(next_task)
        //         index-1             add                     index

        // get pre node, and, next node
        int pre_node = tasks.get(index - 1).to;
        int nex_node = tasks.get(index).from;

        dist += (t.dist + Data.dist[pre_node][t.from] + Data.dist[t.to][nex_node] - Data.dist[pre_node][nex_node]);
        load += t.demand;
        tasks.add(index, t);
        switch (t.type) {
            case NODE -> nodes++;
            case EDGE -> edges++;
            case ARC -> arcs++;
        }
    }

    public void add(List<Task> task_list, int index) {
        while (!task_list.isEmpty()) {
            Task t = task_list.remove(0);
            add(t, index);
            index++;
        }
    }

    public Task remove(int index) {

        // cannot delete depot or out of bounds
        if (index <= 0 || index >= tasks.size() - 1) {
            System.out.println("remove(index wrong) in route");
            System.exit(1);
        }

        // pre_node(pre_task), task(from_node, to_node), nex_node(next_task)
        //         index-1              remove                index+1

        // get pre node, and, next node
        int pre_node = tasks.get(index - 1).to;
        int nex_node = tasks.get(index + 1).from;
        Task t = tasks.remove(index);
        dist += (Data.dist[pre_node][nex_node] - t.dist - Data.dist[pre_node][t.from] - Data.dist[t.to][nex_node]);
        load -= t.demand;
        switch (t.type) {
            case NODE -> nodes--;
            case EDGE -> edges--;
            case ARC -> arcs--;
        }
        return t;
    }

    public void reverse_all_edge_tasks() {
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            if (t.type != TaskType.EDGE) continue;
            remove(i);
            add(Data.get_reverse_edge(t), i);
        }
    }

    @Override
    public String toString() {
        return "Route{" +
                "dist=" + dist +
                ", load=" + load +
                ", tasks=" + tasks +
                "}";
    }
}
