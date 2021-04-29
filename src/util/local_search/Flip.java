package util.local_search;

import structure.*;

public class Flip extends Operator {

    //  "Flip" reverses the direction of all of the edge tasks of a route.
    private int reverse_route_index;

//    @Override
//    public void local_search(Solution s) {
//
//        int[] visited = new int[s.routes.size()];
//        boolean has_valid_move = true;
//
//        while (has_valid_move) {
//            has_valid_move = false;
//            reverse_route_index = -1;
//            best_move_saving = Integer.MAX_VALUE;
//            for (int i = 0; i < s.routes.size(); i++) {
//                if (visited[i] == 1) continue;
//                Route r = s.routes.get(i);
//                Route temp = new Route(r);
//                temp.reverse_all_edge_tasks();
//                int change = temp.dist - r.dist;
//                if (change >= 0) visited[i] = 1;
//                if (change < 0 && change < best_move_saving) {
//                    best_move_saving = change;
//                    reverse_route_index = i;
//                    has_valid_move = true;
//                }
//            }
//            if (has_valid_move) {
//                s.routes.get(reverse_route_index).reverse_all_edge_tasks();
//                visited[reverse_route_index] = 1;
//                System.out.println("reverse edges of route => " + reverse_route_index);
//            }
//        }
//    }

    @Override
    public boolean get_best_move(Solution s) {
        Data data = s.data;
        reverse_route_index = -1;
        best_move_saving = Integer.MAX_VALUE;
        for (int i = 0; i < s.routes.size(); i++) {
            Route r = s.routes.get(i);
//            Route temp = new Route(r);
//            temp.reverse_all_edge_tasks();
            Route temp = new Route(data); // only travel this route once
            for (Task t : r.tasks) {
                if (t.type == TaskType.EDGE) temp.add(data.get_reverse_edge(t));
                else temp.add(t);
            }
            temp.add(data.depot);
            int change = temp.dist - r.dist;
            if (change < 0 && change < best_move_saving) {
                reverse_route_index = i;
                // if there is valid move,
                // save the saving of best move
                best_move_saving = change;
            }
        }
        return reverse_route_index >= 0;
    }

    @Override
    public void do_move(Solution s) {
//        System.out.println("reverse edges of route => " + reverse_route_index);
        s.routes.get(reverse_route_index).reverse_all_edge_tasks();
    }
}
