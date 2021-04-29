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
        String[] dir_paths = new String[]{"bhw", "cbmix"};
        Algorithm algo = new Algorithm();
        try {
            for (String dir_path : dir_paths) {
                Data[] all_data = ReadData.getAll(pre + dir_path);
                for (Data data : all_data) {
//                    data = ReadData.get(pre + dir_path + "/BHW6.dat");
                    data.show();
                    data.preprocess();
                    MyParameter.init(data);
                    Solution sol = algo.run(data);
                    WriteData.write(sol, dir_path);
                    System.out.println(sol);
                    break;
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void config(String type, String arg) {
        switch (type) {
            case "-s":
                if (arg.equals("time")) MyParameter.setRandomSeed();
                else MyParameter.setRandomSeed(Integer.parseInt(arg));
                break;
            case "-t":
                MyParameter.setRunningTime(Double.parseDouble(arg));
                break;
            case "-ls":
                switch (arg) {
                    case "order" -> MyParameter.setLSType(0);
                    case "best" -> MyParameter.setLSType(1);
                    default -> {
                        System.out.println("no such para for ls");
                        System.exit(1);
                    }
                }
                break;
            default:
                System.out.println("no such para");
                System.exit(1);
        }
    }
}
