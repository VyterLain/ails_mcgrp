package util;

import structure.Data;
import structure.Solution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteData {
    public static void write(Solution s, String path) throws IOException {
        s.getDist();
        if (!s.check_feasible()) {
            System.out.println("wrong! infeasible solution occured");
            System.exit(1);
        }
        path = "src/output/" + path + '/' + s.data.name + '_' + MyParameter.running_time + ".txt";
        File wf = new File(path);
        BufferedWriter bw = new BufferedWriter(new FileWriter(wf));
        bw.write(s.toString());
        bw.flush();
        bw.close();
    }
}
