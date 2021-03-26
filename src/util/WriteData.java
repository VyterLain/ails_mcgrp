package util;

import structure.Data;
import structure.Solution;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteData {
    public static void write(Solution s, String path, boolean server) throws IOException {
        s.getDist();
        if (!s.check_feasible()) {
            System.out.println("wrong! infeasible solution occured");
            System.exit(1);
        }
        if (server) path = "/ora1/mcgrp/output/" + path + '/' + Data.name + '_' + MyParameter.running_time + ".txt";
        else path = "src/output/" + path + '/' + Data.name + '_' + MyParameter.running_time + ".txt";
        File wf = new File(path);
        BufferedWriter bw = new BufferedWriter(new FileWriter(wf));
        bw.write(s.toString());
        bw.flush();
        bw.close();
    }
}
