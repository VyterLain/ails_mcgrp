import structure.Data;
import structure.Solution;
import util.MyParameter;
import util.ReadData;
import util.WriteData;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String pre = "src/data/";
        try {
            String[] dir_paths = new String[]{"bhw", "cbmix"};
            for (String dir_path : dir_paths) {
                File dir = new File(pre + dir_path);
                String[] files_name = dir.list();
                assert files_name != null;
                for (String file_name : files_name) {
                    ReadData.get(pre + dir_path + '/' + file_name);
                    Data.show();
                    Data.preprocess();
                    MyParameter.init();
                    Algorithm algo = new Algorithm();
                    Solution sol = algo.run();
                    WriteData.write(sol, dir_path);
                    System.out.println(sol);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
