package experiments;

import utils.Constant;
import utils.FileManipulation;
import utils.Factor;
import wrsn.Map;
import wrsn.WCE;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ExperimentOne {
    public static void main(String[] args) throws IOException {
        Factor factor = new Factor();
        execute(Constant.FILENAME, factor, 1, 6);
    }

    public static void execute(String filename, Factor factor, int scenario_factor, int algorithmNumber) throws IOException {
        System.out.println(filename);
        StringTokenizer st = new StringTokenizer(filename, "\\");
        String actualFileName = "";
        String actualFileDir = "";
        int deepCount = 0;
        while (st.hasMoreTokens()) {
            deepCount ++;
            String string = st.nextToken();
//            if (deepCount >= 15) {
//                actualFileName += "\\" + string;
//            }
            if (deepCount >= 4 && deepCount <= 5) {
                actualFileDir += "\\" + string;
            }
            else if (deepCount == 6) {
                actualFileName = string;
            }
        }
        Constant.RESULT_DIRECTORY_PATH = Constant.RESULT_DIRECTORY_PATH.replace("algorithm", Factor.algorithm[algorithmNumber - 1]);
        Constant.RESULT_DIRECTORY_PATH_TIME = Constant.RESULT_DIRECTORY_PATH_TIME.replace("algorithm", Factor.algorithm[algorithmNumber - 1]);
        Constant.RESULT_E_CHARGE_DIRECTORY_PATH = Constant.RESULT_E_CHARGE_DIRECTORY_PATH.replace("algorithm", Factor.algorithm[algorithmNumber - 1]);
        String resultFilePath = Constant.RESULT_DIRECTORY_PATH + "scenario_" + scenario_factor + "\\" + actualFileDir + "\\" + Constant.RESULT_FILE;
        if (Files.exists(Paths.get(resultFilePath))) {
            File file = new File(resultFilePath);
            Scanner scanner = new Scanner(file);
            String allString = "";
            while (scanner.hasNextLine()) {
                allString += scanner.nextLine();
            }
            String key = actualFileName + " ";
            if (scenario_factor == 2) key += WCE.U;
            else if (scenario_factor == 3) key += WCE.E_MC;
            else if (scenario_factor == 4) key += WCE.V;
            else if (scenario_factor == 5) key += Factor.T;
            else if (scenario_factor == 6) key += WCE.U + " " + WCE.E_MC;
            else if (scenario_factor == 7) key += Factor.P_MULTIPLIER;
            if (!allString.contains(key)) {
                FileManipulation fm = new FileManipulation();
                for (int i = 0; i < Factor.SEEDS; i++) {
                    Map map = new Map();
                    fm.readFile(filename, map, factor);
                    Factor.SEED = i;
                    Algorithm algorithm = run(map, factor, algorithmNumber);
                    fm.writeFile(algorithm, filename, scenario_factor, algorithmNumber);
//                    fm.writeTimeFile(algorithm, filename, scenario_factor, algorithmNumber);
                }
            }
        }
        else {
            FileManipulation fm = new FileManipulation();
            for (int i = 0; i < Factor.SEEDS; i++) {
                Map map = new Map();
                fm.readFile(filename, map, factor);
                Factor.SEED = i;
                Algorithm algorithm = run(map, factor, algorithmNumber);
                fm.writeFile(algorithm, filename, scenario_factor, algorithmNumber);
//                fm.writeTimeFile(algorithm, filename, scenario_factor, algorithmNumber);
            }
        }
    }

    private static Algorithm run(Map map, Factor factor, int algorithmNumber) {
            Algorithm algorithm;
            if (algorithmNumber == 1) {
                algorithm = new HGACMAES();  // First Algorithm
                algorithm.execute(map, factor);
            }
            else if (algorithmNumber == 2) {
                algorithm = new SAMER();
                algorithm.execute(map, factor);
            }
            else if (algorithmNumber == 3) {
                algorithm = new GACS();
                algorithm.execute(map, factor);
            }
            else if (algorithmNumber == 4) {
                algorithm = new FCFS();
                algorithm.execute(map, factor);
            }
            else if (algorithmNumber == 5) {
                algorithm = new PA();
                algorithm.execute(map, factor);
            }
            else if (algorithmNumber == 6) {
                algorithm = new INMA();
                algorithm.execute(map, factor);
            }
            else {
                System.out.println("do nothing.");
                algorithm = new DoNothing();
                algorithm.execute(map, factor);
            }
            return algorithm;
    }
}
