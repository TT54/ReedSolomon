package fr.tt54.qrcodes;

import fr.tt54.qrcodes.finite_fields.F256;
import fr.tt54.qrcodes.polynoms.Polynome256;
import fr.tt54.qrcodes.reedsolomon.Encoder;
import fr.tt54.qrcodes.reedsolomon.ReedSolomon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class DataStudy {

    /*public static void writeFile(){
        File outFile = new File(filePath);
        outFile.getParentFile().mkdirs();

        // FileWriter(File outFile, boolean append)
        try {
            FileWriter fileWriter = new FileWriter(outFile, StandardCharsets.UTF_16);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }*/

    private static DecimalFormat format = new DecimalFormat("00.00%");

    public static long calculateTimeForPolynomialsGeneration(int infoSize, int t, int polynomialAmount){
        int previousT = Encoder.t;
        Encoder.t = t;
        long time = System.currentTimeMillis();


        for(int i = 0; i < polynomialAmount; i++){
            Polynome256 poly = new Polynome256(F256.getZero());
            for (int j = 0; j <= infoSize; j++) {
                poly.setCoeff(j, F256.getElement(ReedSolomon.random.nextInt(256)));
            }

            Polynome256 result = ReedSolomon.encode(poly);
        }

        Encoder.t = previousT;
        return System.currentTimeMillis() - time;
    }



    public static void evaluateComplexityDatas(int minDataSize, int maxDataSize, int minT, int maxT, int tries){
        File outFile = new File("D:\\Theo\\Cours\\Tipe\\Maths\\complexity_datas" + "_" + System.currentTimeMillis() + "_size=" + minDataSize + "," + maxDataSize + "_t=" + minT + "," + maxT + ".csv");
        outFile.getParentFile().mkdirs();

        // FileWriter(File outFile, boolean append)
        try {
            FileWriter fileWriter = new FileWriter(outFile, StandardCharsets.UTF_16);

            fileWriter.write("t,taille des données,nombre d'essais,temps moyen (en ms),temps total (en ms)");
            fileWriter.write("\n");

            for(int t = minT; t <= maxT; t++) {
                System.out.println("Génération des polynômes avec t = " + t);
                for (int size = minDataSize; size <= maxDataSize; size++) {
                    long timeElapsed = calculateTimeForPolynomialsGeneration(size, t, tries);

                    fileWriter.write(t + "," + size + "," + tries + "," + (timeElapsed / tries) + "," + timeElapsed);
                    fileWriter.write("\n");

                    if(((int) (((double) (size - minDataSize) / (maxDataSize - minDataSize)) * 100)) % 10 == 0) {
                        System.out.println(format.format((double) (size - minDataSize) / (maxDataSize - minDataSize)));
                    }
                }
                System.out.println("");
            }

            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static void calculateComplexityOfT(int dataSize, int minT, int maxT, int tries, boolean dataPerTry){
        File outFile = new File("D:\\Theo\\Cours\\Tipe\\Maths\\complexity_datas" + "_" + System.currentTimeMillis() + "_size=" + dataSize + "_t=" + minT + "," + maxT + ".csv");
        outFile.getParentFile().mkdirs();

        // FileWriter(File outFile, boolean append)
        try {
            FileWriter fileWriter = new FileWriter(outFile, StandardCharsets.UTF_16);

            Map<Integer, Long> times = new HashMap<>();
            Map<Integer, Map<Integer, Long>> timesPerPolynomial = new HashMap<>();

            for(int i = 0; i < tries; i++){
                Polynome256 poly = new Polynome256(F256.getZero());
                for (int j = 0; j <= dataSize; j++) {
                    poly.setCoeff(j, F256.getElement(ReedSolomon.random.nextInt(256)));
                }

                int previousT = Encoder.t;

                for(int t = minT; t <= maxT; t++) {
                    Encoder.t = t;
                    long time = System.currentTimeMillis();
                    Polynome256 result = ReedSolomon.encode(poly);
                    time = System.currentTimeMillis() - time;

                    if(dataPerTry){
                        Map<Integer, Long> datas = timesPerPolynomial.getOrDefault(i, new HashMap<>());
                        datas.put(t, time);
                        timesPerPolynomial.put(i, datas);
                    }

                    times.put(t, times.getOrDefault(t, 0L) + time);
                }

                if(tries >= 10 && i % (tries / 10) == 0){
                    System.out.println(format.format((double) i / tries));
                }

                Encoder.t = previousT;
            }

            if(dataPerTry){
                fileWriter.write("t,taille des données,nombre d'essais,temps moyen (en ms),temps total (en ms)");
                for(int i = 0; i < tries; i++){
                    fileWriter.write(",polynome " + (i+1));
                }
                fileWriter.write("\n");
            } else {
                fileWriter.write("t,taille des données,nombre d'essais,temps moyen (en ms),temps total (en ms)");
                fileWriter.write("\n");
            }

            for(int t : times.keySet()){
                long timeElapsed = times.get(t);
                fileWriter.write(t + "," + dataSize + "," + tries + "," + (timeElapsed / tries) + "," + timeElapsed);
                if(dataPerTry){
                    for(int i : timesPerPolynomial.keySet()){
                        fileWriter.write("," + timesPerPolynomial.get(i).get(t));
                    }
                }
                fileWriter.write("\n");
            }

            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
