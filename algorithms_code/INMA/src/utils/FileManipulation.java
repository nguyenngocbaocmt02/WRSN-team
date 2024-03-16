package utils;

import experiments.Algorithm;
import experiments.SAMER;
import wrsn.Map;
import wrsn.Sensor;
import wrsn.WCE;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class FileManipulation {
    public void readFile(String filename, Map map, Factor factor) {
        File file =new File(filename);
        Scanner sc;
        double x = 0, y = 0, p = 0, e = 0; // bien tam thoi
        try {
            sc = new Scanner(file);
            // Doc thong tin cac sensor
            int countN = 0; // dem so luong sensor
//            sc.nextDouble();
            Constant.DEFAULT_X = sc.nextDouble();
            Constant.DEFAULT_Y = sc.nextDouble();
            double sumP = 0;
            while (sc.hasNext()) {
                // dinh dang mot dong trong data file x y p e
                x = sc.nextDouble();
                y = sc.nextDouble();
                p = sc.nextDouble();
                double p_temp = p;
                p = p * Factor.P_MULTIPLIER;
                sumP += p;
                e = sc.nextDouble() - p_temp * Factor.t;
                Sensor s = new Sensor(x, y, p, e);
                map.setSensor(countN, s);
                countN++; // tang so luong sensor them 1
            }
            map.setN(countN);
            Factor.N = (int) (2 * (4 + Math.floor(3 * Math.log(countN)))); // 4+floor(3*log(n)), so ca the trong quan the tinh theo thuat toan cma_es
            Factor.P_BOUND = sumP / countN / 1.5;
            sc.close();
        } catch (FileNotFoundException | NoSuchElementException ex) {
            System.out.println("DMM:" + x + "," + y);
            ex.printStackTrace();
        }
    }

    public void writeFile(Algorithm al, String filename, int scenario_factor, int algorithmNumber) throws IOException {
        Constant.RESULT_DIRECTORY_PATH = Constant.RESULT_DIRECTORY_PATH.replace("algorithm", Factor.algorithm[algorithmNumber - 1]);
        Constant.RESULT_E_MOVE_DIRECTORY_PATH = Constant.RESULT_E_MOVE_DIRECTORY_PATH.replace("algorithm", Factor.algorithm[algorithmNumber - 1]);
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
            actualFileDir += "\\";
            String resultFilePath = "";
            String resultEmoveFilePath = "";
            String resultEchargeFilePath = "";
            resultFilePath = Constant.RESULT_DIRECTORY_PATH + "scenario_" + scenario_factor + "\\" + actualFileDir + Constant.RESULT_FILE;
            resultEmoveFilePath = Constant.RESULT_E_MOVE_DIRECTORY_PATH + "scenario_" + scenario_factor + "\\" + actualFileDir + Constant.RESULT_FILE;
            resultEchargeFilePath = Constant.RESULT_E_CHARGE_DIRECTORY_PATH + "scenario_" + scenario_factor + "\\" + actualFileDir + Constant.RESULT_FILE;
            if (scenario_factor == 1) {
                if (!Files.exists(Paths.get(resultFilePath))) {
                    File resultFile = new File(resultFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 2) {
                if (!Files.exists(Paths.get(resultFilePath))) {
                    File resultFile = new File(resultFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename U #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 3) {
                if (!Files.exists(Paths.get(resultFilePath))) {
                    File resultFile = new File(resultFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename E_MC #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 4) {
                if (!Files.exists(Paths.get(resultFilePath))) {
                    File resultFile = new File(resultFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename V #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 5) {
                if (!Files.exists(Paths.get(resultFilePath))) {
                    File resultFile = new File(resultFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename T #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 6) {
                if (!Files.exists(Paths.get(resultFilePath))) {
                    File resultFile = new File(resultFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename U E #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 7) {
                if (!Files.exists(Paths.get(resultFilePath))) {
                    File resultFile = new File(resultFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename xP #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            if (scenario_factor == 1) {
                if (!Files.exists(Paths.get(resultEmoveFilePath))) {
                    File resultFile = new File(resultEmoveFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 2) {
                if (!Files.exists(Paths.get(resultEmoveFilePath))) {
                    File resultFile = new File(resultEmoveFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename U #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 3) {
                if (!Files.exists(Paths.get(resultEmoveFilePath))) {
                    File resultFile = new File(resultEmoveFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename E_MC #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 4) {
                if (!Files.exists(Paths.get(resultEmoveFilePath))) {
                    File resultFile = new File(resultEmoveFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename V #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 5) {
                if (!Files.exists(Paths.get(resultEmoveFilePath))) {
                    File resultFile = new File(resultEmoveFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename T #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 6) {
                if (!Files.exists(Paths.get(resultEmoveFilePath))) {
                    File resultFile = new File(resultEmoveFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename U E #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 7) {
                if (!Files.exists(Paths.get(resultEmoveFilePath))) {
                    File resultFile = new File(resultEmoveFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename xP #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            
            
            if (scenario_factor == 1) {
                if (!Files.exists(Paths.get(resultEchargeFilePath))) {
                    File resultFile = new File(resultEchargeFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 2) {
                if (!Files.exists(Paths.get(resultEchargeFilePath))) {
                    File resultFile = new File(resultEchargeFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename U #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 3) {
                if (!Files.exists(Paths.get(resultEchargeFilePath))) {
                    File resultFile = new File(resultEchargeFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename E_MC #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 4) {
                if (!Files.exists(Paths.get(resultEchargeFilePath))) {
                    File resultFile = new File(resultEchargeFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename V #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 5) {
                if (!Files.exists(Paths.get(resultEchargeFilePath))) {
                    File resultFile = new File(resultEchargeFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename T #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 6) {
                if (!Files.exists(Paths.get(resultEchargeFilePath))) {
                    File resultFile = new File(resultEchargeFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename U E #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
            else if (scenario_factor == 7) {
                if (!Files.exists(Paths.get(resultEchargeFilePath))) {
                    File resultFile = new File(resultEchargeFilePath);
                    resultFile.getParentFile().mkdirs();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                    writer.write("filename xP #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                    writer.newLine();
                    writer.close();
                }
            }
//            String resultFilePath = Constant.RESULT_DIRECTORY_PATH + actualFileName;
            File file =new File(resultFilePath);
            file.getParentFile().mkdirs();
            FileWriter f = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(f);

//            String line = "Cac nut duoc sac ";
//            for (int sensor  ((SAMER) al).getChargedNodes()) {
//                line += sensor + ", ";
//            }
//            writer.write(line);
//            writer.newLine();
//
//            line = "Cac nut chet ";
//            for (int sensor  ((SAMER) al).getDeadNodes()) {
//                line += sensor + ", ";
//            }
//            writer.write(line);
//            writer.newLine();
//
//            line = "So nut chet " + ((SAMER) al).getDeadNodes().size() + ".";
            String line = "";
            if (Factor.SEED == 0) {
                line += actualFileName + " ";
                if (scenario_factor == 2)
                    line += WCE.U + " ";
                else if (scenario_factor == 3)
                    line += WCE.E_MC + " ";
                else if (scenario_factor == 4)
                    line += WCE.V + " ";
                else if (scenario_factor == 5)
                    line += Factor.T + " ";
                else if (scenario_factor == 6)
                    line += WCE.U + " " + WCE.E_MC + " ";
                else if (scenario_factor == 7)
                    line += Factor.P_MULTIPLIER + " ";
//                    line += "1/" + (int)Factor.P_MULTIPLIER + " ";
            }
            line += al.getDeadNodes().size() + " ";
            writer.write(line);
            if (Factor.SEED == 9)
                writer.newLine();
            writer.close();

        File fileEmove =new File(resultEmoveFilePath);
        file.getParentFile().mkdirs();
        FileWriter fEmove = new FileWriter(fileEmove, true);
        BufferedWriter writerEmove = new BufferedWriter(fEmove);

//            String line = "Cac nut duoc sac ";
//            for (int sensor  ((SAMER) al).getChargedNodes()) {
//                line += sensor + ", ";
//            }
//            writer.write(line);
//            writer.newLine();
//
//            line = "Cac nut chet ";
//            for (int sensor  ((SAMER) al).getDeadNodes()) {
//                line += sensor + ", ";
//            }
//            writer.write(line);
//            writer.newLine();
//
//            line = "So nut chet " + ((SAMER) al).getDeadNodes().size() + ".";
        String lineEmove = "";
        if (Factor.SEED == 0) {
            lineEmove += actualFileName + " ";
            if (scenario_factor == 2)
                lineEmove += WCE.U + " ";
            else if (scenario_factor == 3)
                lineEmove += WCE.E_MC + " ";
            else if (scenario_factor == 4)
                lineEmove += WCE.V + " ";
            else if (scenario_factor == 5)
                lineEmove += Factor.T + " ";
            else if (scenario_factor == 6)
                lineEmove += WCE.U + " " + WCE.E_MC + " ";
            else if (scenario_factor == 7)
                lineEmove += Factor.P_MULTIPLIER + " ";
//                    line += "1/" + (int)Factor.P_MULTIPLIER + " ";
        }
        lineEmove += al.sumMove + " ";
        writerEmove.write(lineEmove);
        if (Factor.SEED == 9)
            writerEmove.newLine();
        writerEmove.close();
        
        File fileEcharge =new File(resultEchargeFilePath);
        file.getParentFile().mkdirs();
        FileWriter fEcharge = new FileWriter(fileEcharge, true);
        BufferedWriter writerEcharge = new BufferedWriter(fEcharge);
        String lineEcharge = "";
        if (Factor.SEED == 0) {
        	lineEcharge += actualFileName + " ";
            if (scenario_factor == 2)
            	lineEcharge += WCE.U + " ";
            else if (scenario_factor == 3)
            	lineEcharge += WCE.E_MC + " ";
            else if (scenario_factor == 4)
            	lineEcharge += WCE.V + " ";
            else if (scenario_factor == 5)
            	lineEcharge += Factor.T + " ";
            else if (scenario_factor == 6)
            	lineEcharge += WCE.U + " " + WCE.E_MC + " ";
            else if (scenario_factor == 7)
            	lineEcharge += Factor.P_MULTIPLIER + " ";
//                    line += "1/" + (int)Factor.P_MULTIPLIER + " ";
        }
        lineEcharge += al.sumCharge + " ";
        writerEcharge.write(lineEcharge);
        if (Factor.SEED == 9)
            writerEcharge.newLine();
        writerEcharge.close();
    }

    public void writeTimeFile(Algorithm al, String filename, int scenario_factor, int algorithmNumber) throws IOException {
        Constant.RESULT_DIRECTORY_PATH_TIME = Constant.RESULT_DIRECTORY_PATH_TIME.replace("algorithm", Factor.algorithm[algorithmNumber - 1]);
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
        actualFileDir += "\\";
        String resultFilePath = "";
        resultFilePath = Constant.RESULT_DIRECTORY_PATH_TIME + "scenario_" + scenario_factor + "\\" + actualFileDir + Constant.RESULT_FILE;
        if (scenario_factor == 1) {
            if (!Files.exists(Paths.get(resultFilePath))) {
                File resultFile = new File(resultFilePath);
                resultFile.getParentFile().mkdirs();
                BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                writer.write("filename #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                writer.newLine();
                writer.close();
            }
        }
        else if (scenario_factor == 2) {
            if (!Files.exists(Paths.get(resultFilePath))) {
                File resultFile = new File(resultFilePath);
                resultFile.getParentFile().mkdirs();
                BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                writer.write("filename U #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                writer.newLine();
                writer.close();
            }
        }
        else if (scenario_factor == 3) {
            if (!Files.exists(Paths.get(resultFilePath))) {
                File resultFile = new File(resultFilePath);
                resultFile.getParentFile().mkdirs();
                BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                writer.write("filename E_MC #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                writer.newLine();
                writer.close();
            }
        }
        else if (scenario_factor == 4) {
            if (!Files.exists(Paths.get(resultFilePath))) {
                File resultFile = new File(resultFilePath);
                resultFile.getParentFile().mkdirs();
                BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                writer.write("filename V #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                writer.newLine();
                writer.close();
            }
        }
        else if (scenario_factor == 5) {
            if (!Files.exists(Paths.get(resultFilePath))) {
                File resultFile = new File(resultFilePath);
                resultFile.getParentFile().mkdirs();
                BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                writer.write("filename T #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                writer.newLine();
                writer.close();
            }
        }
        else if (scenario_factor == 6) {
            if (!Files.exists(Paths.get(resultFilePath))) {
                File resultFile = new File(resultFilePath);
                resultFile.getParentFile().mkdirs();
                BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                writer.write("filename U E #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                writer.newLine();
                writer.close();
            }
        }
        else if (scenario_factor == 7) {
            if (!Files.exists(Paths.get(resultFilePath))) {
                File resultFile = new File(resultFilePath);
                resultFile.getParentFile().mkdirs();
                BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, true));
                writer.write("filename xP #1 #2 #3 #4 #5 #6 #7 #8 #9 #10");
                writer.newLine();
                writer.close();
            }
        }
//            String resultFilePath = Constant.RESULT_DIRECTORY_PATH + actualFileName;
        File file =new File(resultFilePath);
        file.getParentFile().mkdirs();
        FileWriter f = new FileWriter(file, true);
        BufferedWriter writer = new BufferedWriter(f);

//            String line = "Cac nut duoc sac ";
//            for (int sensor  ((SAMER) al).getChargedNodes()) {
//                line += sensor + ", ";
//            }
//            writer.write(line);
//            writer.newLine();
//
//            line = "Cac nut chet ";
//            for (int sensor  ((SAMER) al).getDeadNodes()) {
//                line += sensor + ", ";
//            }
//            writer.write(line);
//            writer.newLine();
//
//            line = "So nut chet " + ((SAMER) al).getDeadNodes().size() + ".";
        String line = "";
        if (Factor.SEED == 0) {
            line += actualFileName + " ";
            if (scenario_factor == 2)
                line += WCE.U + " ";
            else if (scenario_factor == 3)
                line += WCE.E_MC + " ";
            else if (scenario_factor == 4)
                line += WCE.V + " ";
            else if (scenario_factor == 5)
                line += Factor.T + " ";
            else if (scenario_factor == 6)
                line += WCE.U + " " + WCE.E_MC + " ";
            else if (scenario_factor == 7)
                line += Factor.P_MULTIPLIER + " ";
//                    line += "1/" + (int)Factor.P_MULTIPLIER + " ";
        }
        line += al.getExecutedTime() + " ";
        writer.write(line);
        if (Factor.SEED == 9)
            writer.newLine();
        writer.close();
    }

    public void writeFile(Algorithm al, int algorithmNumber) throws IOException {
        if (algorithmNumber == 1) {
        }
        else if (algorithmNumber == 2) {
            String resultFilePath = Constant.RESULT_FILE;
            File file =new File(resultFilePath);
            file.getParentFile().mkdirs();
            FileWriter f = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(f);

            String line = ((SAMER) al).getDeadNodes().size() + " ";
            writer.write(line);
            writer.close();
        }
        else {
        }
    }
}
