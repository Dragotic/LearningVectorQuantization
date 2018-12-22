package generators;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class ClassificationDataGenerator {

    private static Map<Float, Float> samples1 = new HashMap<>();
    private static Map<Float, Float> samples2 = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Random rnd = new Random();

        // Training Set - Generate a value in [0, 1]
        IntStream.range(0, 750)
                .forEach(value -> {
                    samples1.put(rnd.nextFloat(), rnd.nextFloat());
                });

        IntStream.range(0, 750)
                .forEach(value -> {
                    samples2.put(rnd.nextFloat(), rnd.nextFloat());
                });

        // Test Set - Generate a value in [-1, 0]
        IntStream.range(0, 750)
                .forEach(value -> {
                    samples1.put(rnd.nextFloat() -1, rnd.nextFloat() -1);
                });

        IntStream.range(0, 750)
                .forEach(value -> {
                    samples2.put(rnd.nextFloat() -1, rnd.nextFloat() -1);
                });

        writeToFile("classification_training_data.txt", samples1);
        writeToFile("classification_test_data.txt", samples2);
    }

    private static void writeToFile(String filename, Map<Float, Float> data) throws IOException {
        BufferedWriter bufferedWriter= new BufferedWriter(new FileWriter(
                new File(filename)));
        // gia kathe zeugos
        data.forEach((x, y) -> {
            String result = "";

            // Sinthiki 1 (x-0.5)^2 + (y-0.5)^2  < 0.16
            if (Math.pow((x - 0.5), 2) + Math.pow((y - 0.5), 2) < 0.16) {
                result = "C1";
            }
            // Sinthiki 2 (x + 0.5)^2 + (y + 0.5)^2  < 0.16
            else if (Math.pow((x + 0.5), 2) + Math.pow((y + 0.5), 2) < 0.16) {
                result = "C2";
            }
            // Alliws Sinthiki 3
            else {
                result = "C3";
            }
            StringBuilder builder = new StringBuilder().append(x)
                    .append(",")
                    .append(y)
                    .append(",")
                    .append(result);

            // Append sto arxeio x,y,kathgoria
            try {
                bufferedWriter.write(builder.toString() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        bufferedWriter.close();
    }
}

