package advisor;


import advisor.logic.RunTime;
import advisor.logic.Config;

public class Main {
    public static void main(String[] args) {
        try {
            if (args != null) {
                for (int i = 0; i < args.length - 1; i++) {
                    switch (args[i]) {
                        case "-access":
                            Config.setAccessServer(args[i + 1]);
                            break;
                        case "-resource":
                            Config.setResourceServer(args[i + 1]);
                            break;
                        case "-page":
                            Config.setPageSize(Integer.parseInt(args[i + 1]));
                            break;
                    }
                }
            }
            RunTime.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
