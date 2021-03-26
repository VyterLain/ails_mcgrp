package util.destroy;

import structure.Solution;
import structure.Task;
import util.MyParameter;

import java.util.List;
import java.util.Random;

public interface Destructor {

    public Random random = MyParameter.random;

    public List<Task> destruct(int k, Solution sol);
}
