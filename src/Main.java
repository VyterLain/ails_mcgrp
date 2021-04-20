import structure.Data;
import structure.Solution;
import util.MyParameter;
import util.ReadData;
import util.WriteData;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        if (args.length % 2 != 0) {
            System.out.println("wrong args!");
            System.exit(1);
        } else for (int i = 0; i < args.length; i = i + 2) config(args[i], args[i + 1]);
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

    private static void config(String type, String arg) {
        if (type.equals("-s")) MyParameter.setRandomSeed(Integer.parseInt(arg));
        else if (type.equals("-t")) MyParameter.setRunningTime(Double.parseDouble(arg));
        else {
            System.out.println("no such para");
            System.exit(1);
        }
    }
}
