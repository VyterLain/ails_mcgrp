package util.destroy;

import structure.Data;
import structure.Solution;
import structure.Task;
import util.MyParameter;

import java.util.List;
import java.util.Random;

public interface Destructor {

    Random random = MyParameter.random;

    List<Task> destruct(Data data, int k, Solution sol);
}
