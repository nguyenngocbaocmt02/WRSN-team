//package experiments;
//
//import utils.Constant;
//import utils.Factor;
//import wrsn.Map;
//import wrsn.WCE;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Objects;
//
//public class ExperimentAll {
//    public static void main(String[] args) throws IOException {
//        Map map = new Map();
//        Factor factor = new Factor();
////        execute(Constant.DATA_DIRECTORY_PATH, factor, 7);
//        for (int i : Factor.scenario_factor) {
//            execute(Constant.DATA_DIRECTORY_PATH, factor, i);
//        }
//    }
//
//    private static void execute(String dataDirectory, Factor factor, int scenario) throws IOException {
//        fileSearching(dataDirectory, factor, scenario);
//    }
//
//    private static void fileSearching(String dataDirectory, Factor factor, int scenario) throws IOException {
//        int algorithm = 6;
//        File data = new File(dataDirectory);
//        for(File subFile : sortFileArray(data.listFiles())) {
//            if (subFile.isFile()) {
//                if (subFile.getName().contains("xlsx")) continue;
//                if (scenario == 1) {
//                    Factor.P_MULTIPLIER = Factor.P_DEFAULT;
//                    WCE.E_MC = Factor.E_MC_DEFAULT;
//                    WCE.V = Factor.V_DEFAULT;
//                    WCE.U = Factor.U_DEFAULT;
//                    Factor.T = Factor.T_DEFAULT;
//                    System.out.println("\n\n\n\n---------------------Scenario_1: VariantsOfNumberOfNodes---------------------\n\n\n\n");
//
//                    ExperimentOne.execute(subFile.getPath(), factor, scenario, algorithm);
//                }
//                else if (scenario == 2) {
//                    Factor.P_MULTIPLIER = Factor.P_DEFAULT;
//                    WCE.E_MC = Factor.E_MC_DEFAULT;
//                    WCE.V = Factor.V_DEFAULT;
//                    Factor.T = Factor.T_DEFAULT;
//                    for (int u : Factor.Us) {
//
//                        WCE.U = u;
//                        System.out.println("\n\n\n\n---------------------Scenario_2 U = " + WCE.U + "---------------------\n\n\n\n");
//
//                        ExperimentOne.execute(subFile.getPath(), factor, scenario, algorithm);
//                    }
//                }
//                else if (scenario == 3) {
//                    Factor.P_MULTIPLIER = Factor.P_DEFAULT;
//                    WCE.U = Factor.U_DEFAULT;
//                    WCE.V = Factor.V_DEFAULT;
//                    Factor.T = Factor.T_DEFAULT;
//                    for (int e_mc : Factor.E_MCs) {
//
//                        WCE.E_MC = e_mc;
//                        System.out.println("\n\n\n\n---------------------Scenario_3 E_MC = " + WCE.E_MC + "---------------------\n\n\n\n");
//
//                        ExperimentOne.execute(subFile.getPath(), factor, scenario, algorithm);
//                    }
//                }
//                else if (scenario == 4) {
//                    Factor.P_MULTIPLIER = Factor.P_DEFAULT;
//                    WCE.U = Factor.U_DEFAULT;
//                    WCE.E_MC = Factor.E_MC_DEFAULT;
//                    Factor.T = Factor.T_DEFAULT;
//                    for (int v : Factor.Vs) {
//
//                        WCE.V = v;
//                        System.out.println("\n\n\n\n---------------------Scenario_4 V = " + WCE.V + "---------------------\n\n\n\n");
//
//                        ExperimentOne.execute(subFile.getPath(), factor, scenario, algorithm);
//                    }
//                }
//                else if (scenario == 5) {
//                    Factor.P_MULTIPLIER = Factor.P_DEFAULT;
//                    WCE.U = Factor.U_DEFAULT;
//                    WCE.V = Factor.V_DEFAULT;
//                    WCE.E_MC = Factor.E_MC_DEFAULT;
//                    for (int t : Factor.Ts) {
//
//                        Factor.T = t;
//                        System.out.println("\n\n\n\n---------------------Scenario_5 T = " + Factor.T + "---------------------\n\n\n\n");
//
//                        ExperimentOne.execute(subFile.getPath(), factor, scenario, algorithm);
//                    }
//                }
//                else if (scenario == 6) {
//                    Factor.P_MULTIPLIER = Factor.P_DEFAULT;
//                    WCE.V = Factor.V_DEFAULT;
//                    Factor.T = Factor.T_DEFAULT;
//                    for (int u : Factor.Us) {
//                        for (int e_mc: Factor.E_MCs) {
//
//                            WCE.U = u;
//                            WCE.E_MC = e_mc;
//                            System.out.println("\n\n\n\n---------------------Scenario_6 U = " + WCE.U + ", E_MC = " + WCE.E_MC + "---------------------\n\n\n\n");
//
//                            ExperimentOne.execute(subFile.getPath(), factor, scenario, algorithm);
//                        }
//                    }
//                }
//                else if (scenario == 7) {
//                    WCE.V = Factor.V_DEFAULT;
//                    WCE.U = Factor.U_DEFAULT;
//                    WCE.E_MC = Factor.E_MC_DEFAULT;
//                    Factor.T = Factor.T_DEFAULT;
//                    for (double p : Factor.Ps) {
//                        Factor.P_MULTIPLIER = p;
//                        System.out.println("\n\n\n\n---------------------Scenario_7: P = " + Factor.P_MULTIPLIER + "*P" + "---------------------\n\n\n\n");
//
//                        ExperimentOne.execute(subFile.getPath(), factor, scenario, algorithm);
//                    }
//                }
//            } else {
//                fileSearching(dataDirectory + "//" + subFile.getName(), factor, scenario);
//            }
//        }
//    }
//    private static File[] sortFileArray(File[] files) {
//        if (files == null || files.length == 0) {
//            return null;
//        }
//
//        for (File file : files) {
//            if (!file.isFile()) return files;
//        }
//
//        Arrays.sort(files, (o1, o2) -> {
//            // find the dots.
////            int pointIndex1 = o1.getName().lastIndexOf(".");
////            int pointIndex2 = o2.getName().lastIndexOf(".");
//
////            // filename -> integer value.
////            String string1 = o1.getName().replace("_", ".").substring(2, pointIndex1);
////            String string2 = o2.getName().replace("_", ".").substring(2, pointIndex2);
//
//
//            // find the dots.
//            int pointIndex1 = o1.getName().replace("_", ".").lastIndexOf(".");
//            int pointIndex2 = o2.getName().replace("_", ".").lastIndexOf(".");
//            int cut = 0;
//            if (o1.getName().contains("gr") || o1.getName().contains("ga")) {
//                cut = 2;
//            }
//            else {
//                cut = 1;
//            }
//
//            String string1 = o1.getName().replace("_", ".").substring(cut, pointIndex1);
//            String string2 = o2.getName().replace("_", ".").substring(cut, pointIndex2);
//            pointIndex1 = string1.lastIndexOf(".0");
//            pointIndex2 = string2.lastIndexOf(".0");
//            int pointIndex1_2 = string1.lastIndexOf(".s");
//            int pointIndex2_2 = string2.lastIndexOf(".s");
//            int val1_2 = Integer.parseInt(string1.substring(pointIndex1 + 2, pointIndex1_2));
//            int val2_2 = Integer.parseInt(string2.substring(pointIndex2 + 2, pointIndex2_2));
//            string1 = string1.substring(0, pointIndex1);
//            string2 = string2.substring(0, pointIndex2);
//
//            int val1 = Integer.parseInt(string1) + val1_2;
//            int val2 = Integer.parseInt(string2) + val2_2;
//
//            return Integer.compare(val1, val2);
//        });
//
//        return files;
//    }
//}

package experiments;

import utils.Constant;
import utils.Factor;
import wrsn.Map;
import wrsn.WCE;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class ExperimentAll {
    public static void main(String[] args) throws IOException {
        Map map = new Map();
        Factor factor = new Factor();
        execute(Constant.DATA_DIRECTORY_PATH, factor, 1);
//        for (int i : Factor.scenario_factor) {
//            execute(Constant.DATA_DIRECTORY_PATH, factor, i);
//        }
    }

    private static void execute(String dataDirectory, Factor factor, int scenario) throws IOException {
        if (scenario == 1) {
            Factor.P_MULTIPLIER = Factor.P_MULTIPLIER_1;
            System.out.println("\n\n\n\n---------------------Scenario_1: VariantsOfNumberOfNodes---------------------\n\n\n\n");
            fileSearching(dataDirectory, factor, scenario);
        }
        else if (scenario == 2) {
            Factor.P_MULTIPLIER = Factor.P_MULTIPLIER_2;
            WCE.E_MC = Factor.E_MC_DEFAULT;
            WCE.V = Factor.V_DEFAULT;
            Factor.T = Factor.T_DEFAULT;
            for (int u : Factor.Us) {

                WCE.U = u;
                System.out.println("\n\n\n\n---------------------Scenario_2 U = " + WCE.U + "---------------------\n\n\n\n");
                fileSearching(dataDirectory, factor, scenario);
            }
        }
        else if (scenario == 3) {
            Factor.P_MULTIPLIER = Factor.P_DEFAULT;
            WCE.U = Factor.U_DEFAULT;
            WCE.V = Factor.V_DEFAULT;
            Factor.T = Factor.T_DEFAULT;
            for (int e_mc : Factor.E_MC_2s) {

                WCE.E_MC = e_mc;
                System.out.println("\n\n\n\n---------------------Scenario_3 E_MC = " + WCE.E_MC + "---------------------\n\n\n\n");
                fileSearching(dataDirectory, factor, scenario);
            }
        }
        else if (scenario == 4) {
            Factor.P_MULTIPLIER = Factor.P_DEFAULT;
            WCE.U = Factor.U_DEFAULT;
            WCE.E_MC = Factor.E_MC_DEFAULT;
            Factor.T = Factor.T_DEFAULT;
            for (int v : Factor.Vs) {

                WCE.V = v;
                System.out.println("\n\n\n\n---------------------Scenario_4 V = " + WCE.V + "---------------------\n\n\n\n");
                fileSearching(dataDirectory, factor, scenario);
            }
        }
        else if (scenario == 5) {
            Factor.P_MULTIPLIER = Factor.P_DEFAULT;
            WCE.U = Factor.U_DEFAULT;
            WCE.V = Factor.V_DEFAULT;
            WCE.E_MC = Factor.E_MC_DEFAULT;
            for (int t : Factor.Ts) {

                Factor.T = t;
                System.out.println("\n\n\n\n---------------------Scenario_5 T = " + Factor.T + "---------------------\n\n\n\n");
                fileSearching(dataDirectory, factor, scenario);
            }
        }
        else if (scenario == 6) {
            Factor.P_MULTIPLIER = Factor.P_DEFAULT;
            WCE.V = Factor.V_DEFAULT;
            Factor.T = Factor.T_DEFAULT;
//            for (int u : Factor.Us) {
//                for (int e_mc: Factor.E_MCs) {
//
//                    WCE.U = u;
//                    WCE.E_MC = e_mc;
//                    System.out.println("\n\n\n\n---------------------Scenario_6 U = " + WCE.U + ", E_MC = " + WCE.E_MC + "---------------------\n\n\n\n");
//                    fileSearching(dataDirectory, factor, scenario);
//                }
//            }
            for (int i = 0; i < Factor.Us.length; i++) {
                WCE.U = Factor.Us[i];
                WCE.E_MC = Factor.E_MC_1s[i];
                System.out.println("\n\n\n\n---------------------Scenario_6 U = " + WCE.U + ", E_MC = " + WCE.E_MC + "---------------------\n\n\n\n");
                fileSearching(dataDirectory, factor, scenario);
            }
        }
        else if (scenario == 7) {
            WCE.V = Factor.V_DEFAULT;
            WCE.U = Factor.U_DEFAULT;
            WCE.E_MC = Factor.E_MC_DEFAULT;
            Factor.T = Factor.T_DEFAULT;
            for (double p : Factor.Ps) {
                Factor.P_MULTIPLIER = p;
                System.out.println("\n\n\n\n---------------------Scenario_7: P = " + Factor.P_MULTIPLIER + "*P" + "---------------------\n\n\n\n");
                fileSearching(dataDirectory, factor, scenario);
            }
        }
    }

    private static void fileSearching(String dataDirectory, Factor factor, int scenario) throws IOException {
        File data = new File(dataDirectory);
//        for(File subFile : sortFileArray(data.listFiles())) {
        for(File subFile : data.listFiles()) {
            if (subFile.isFile()) {
                if (subFile.getName().contains("coordinates")) break;
                ExperimentOne.execute(subFile.getPath(), factor, scenario, 6);
            } else {
                fileSearching(dataDirectory + "//" + subFile.getName(), factor, scenario);
            }
        }
    }
    private static File[] sortFileArray(File[] files) {
        if (files == null || files.length == 0) {
            return null;
        }

        for (File file : files) {
            if (!file.isFile()) return files;
        }

        Arrays.sort(files, (o1, o2) -> {
            // find the dots.
//            int pointIndex1 = o1.getName().lastIndexOf(".");
//            int pointIndex2 = o2.getName().lastIndexOf(".");

//            // filename -> integer value.
//            String string1 = o1.getName().replace("_", ".").substring(2, pointIndex1);
//            String string2 = o2.getName().replace("_", ".").substring(2, pointIndex2);


            // find the dots.
            int pointIndex1 = o1.getName().replace("_", ".").lastIndexOf(".");
            int pointIndex2 = o2.getName().replace("_", ".").lastIndexOf(".");
            int cut = 0;
            if (o1.getName().contains("gr") || o1.getName().contains("ga")) {
                cut = 2;
            }
            else {
                cut = 1;
            }

            String string1 = o1.getName().replace("_", ".").substring(cut, pointIndex1);
            String string2 = o2.getName().replace("_", ".").substring(cut, pointIndex2);
            pointIndex1 = string1.lastIndexOf(".0");
            pointIndex2 = string2.lastIndexOf(".0");
            int pointIndex1_2 = string1.lastIndexOf(".s");
            int pointIndex2_2 = string2.lastIndexOf(".s");
            int val1_2 = Integer.parseInt(string1.substring(pointIndex1 + 2, pointIndex1_2));
            int val2_2 = Integer.parseInt(string2.substring(pointIndex2 + 2, pointIndex2_2));
            string1 = string1.substring(0, pointIndex1);
            string2 = string2.substring(0, pointIndex2);

            int val1 = Integer.parseInt(string1) + val1_2;
            int val2 = Integer.parseInt(string2) + val2_2;

            return Integer.compare(val1, val2);
        });

        return files;
    }
}

