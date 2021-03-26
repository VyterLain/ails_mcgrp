package util.local_search;

import structure.Solution;

public abstract class Operator {

    /**
     *      if (get_best_move(s, type)){
     *          do_move(s, type);
     *      }
     */

    // saving = new cost - old cost
    // when saving < 0, it is a acceptable move
    // if get_best_move() returns false,
    // "best_move_saving" is meaningless
    public int best_move_saving;

    /**
     * find and do the best move to the current solution
     * until there is no more moves with positive saving
     */
    public abstract void local_search(Solution s);

    /**
     * find the best move and record the move
     * if find successfully, return true
     */
    public abstract boolean get_best_move(Solution s);

    /**
     * do the best move
     */
    public abstract void do_move(Solution s);
}
