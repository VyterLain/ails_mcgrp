package util;

import structure.Data;
import structure.Solution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteData {
    public static void write(Solution s, Data data) throws IOException {
        s.getDist();
        if (!s.check_feasible()) {
            System.out.println(data.name + " wrong! infeasible solution occured");
            return;
        }
        String path = "src/output/" + data.name + '_' + (int) MyParameter.running_time + ".txt";
        File wf = new File(path);
        BufferedWriter bw = new BufferedWriter(new FileWriter(wf));
        bw.write(s.toString());
        bw.flush();
        bw.close();
    }
}
