package ua.nulp.rozrhakha;


import ua.nulp.rozrhakha.ga.Generation;
import ua.nulp.rozrhakha.ga.MeansInstance;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {


        List<Integer> epoches = Arrays.asList(10, 20, 50, 100, 200, 500);
        List<Integer> sizes = Arrays.asList(100, 200, 500, 1000, 2000, 5000);

        StringBuilder builder = new StringBuilder();
        for (Integer epoch: epoches) {
            for (Integer size: sizes) {
                int i = 4;

                while (i <= 4) {
                    Generation.setThreadNum(i);
                    long startTime = System.currentTimeMillis();

                    Generation.setSize(500);
                    Generation.setFunction((x) -> x[0] * x[0] + x[1] * x[1], 2);
                    Generation.setInstancePattern(new MeansInstance());
                    Generation.setIsParallel(true);
                    Generation firstGen = Generation.generateFirstGeneration();
                    Generation lastGen = Generation.evolution(firstGen, epoch);

                    double time = (System.currentTimeMillis() - startTime) / 1000.0;


                    builder.append(time);
                    builder.append(",");

                    i *= 2;
                }

            }
            builder.append("\n");
        }

        try (FileWriter writer = new FileWriter("output.csv")){
            writer.write(builder.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
