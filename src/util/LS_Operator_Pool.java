package util;

import structure.Solution;
import util.local_search.*;

public class LS_Operator_Pool {

    // here, two way to implement
    // 1. run all operator one by one
    // 2. every time we select the best operator among them to run
    // the second is easier to implement
    // here, we use the second way,
    // and we still offer possibility of the first way in our operators
    // (by "get_best_move" and "do_move" methods and "best_move_saving" attribute

    private final Operator flip = new Flip();
    private final Operator swap = new Swap();
    private final Operator or_opt = new Or_opt();
    private final Operator two_opt = new Two_opt();
    private final Operator three_opt = new Three_opt();

    public Solution LS_Full(Solution sol) {
        ((Or_opt) or_opt).set_l(3);
        ((Three_opt) three_opt).set_Max_B_length(3);
        or_opt.local_search(sol);
        swap.local_search(sol);
        two_opt.local_search(sol);
        three_opt.local_search(sol);
        flip.local_search(sol);
        return sol;
    }

    public void LS_1(Solution sol) {
        ((Three_opt) three_opt).set_Max_B_length(1);
        swap.local_search(sol);
        two_opt.local_search(sol);
        three_opt.local_search(sol);
        flip.local_search(sol);
    }

    public void LS_2(Solution sol) {
        ((Or_opt) or_opt).set_l(2);
        or_opt.local_search(sol);
        swap.local_search(sol);
        two_opt.local_search(sol);
    }
}
