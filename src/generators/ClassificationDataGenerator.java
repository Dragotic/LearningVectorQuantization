package generators;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class ClassificationDataGenerator {

    private static Map<Float, Float> trainSet = new HashMap<>();
    private static Map<Float, Float> testSet = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Random rnd = new Random();

        // Training Set - Generate a value in [0, 2]
        IntStream.range(0, 1500)
                .forEach(value -> {
                    trainSet.put(rnd.nextFloat() * 2, rnd.nextFloat() * 2);
                });

        IntStream.range(0, 1500)
                .forEach(value -> {
                    testSet.put(rnd.nextFloat() * 2, rnd.nextFloat() * 2);
                });

        // Test Set - Generate a value in [-2, 0]
        IntStream.range(0, 1500)
                .forEach(value -> {
                    trainSet.put((rnd.nextFloat() * 2) - 2, (rnd.nextFloat() * 2) - 2);
                });

        IntStream.range(0, 1500)
                .forEach(value -> {
                    testSet.put((rnd.nextFloat() * 2) - 2, (rnd.nextFloat() * 2) - 2);
                });

        writeToFile("classification_training_data.txt", trainSet);
        writeToFile("classification_test_data.txt", testSet);
    }

    private static void writeToFile(String filename, Map<Float, Float> data) throws IOException {
        BufferedWriter bufferedWriter= new BufferedWriter(new FileWriter(
                new File(filename)));
        // For each pair (x, y)
        data.forEach((x, y) -> {
            String result = "";

            // Condition 1, 2: (x - 1) ^ 2 + (y - 1) ^ 2 <= 0.16 OR (x + 1) ^ 2 + (y + 1) ^ 2 <= 0.16
            if ((Math.pow((x - 1), 2) + Math.pow((y - 1), 2) <= 0.16) || Math.pow((x + 1), 2) + Math.pow((y + 1), 2) <= 0.16) {
                result = "C1";
            }
            // Condition 3: (x - 1) ^ 2 + (y - 1) ^ 2 > 0.16 AND (x - 1) ^ 2 + (y - 1) ^ 2 < 0.64
            else if ((Math.pow((x - 1), 2) + Math.pow((y - 1), 2) > 0.16) && (Math.pow((x - 1), 2) + Math.pow((y - 1), 2) < 0.64)) {
                result = "C2";
            }
            // Condition 4: (x + 1) ^ 2 + (y + 1) ^ 2 > 0.16 AND (x + 1) ^ 2 + (y + 1) ^ 2 < 0.64
            else if ((Math.pow((x + 1), 2) + Math.pow((y + 1), 2) > 0.16) && (Math.pow((x + 1), 2) + Math.pow((y + 1), 2) < 0.64)) {
                result = "C2";
            }
            // Else category = C3
            else {
                result = "C3";
            }
            StringBuilder builder = new StringBuilder().append(x)
                    .append(",")
                    .append(y)
                    .append(",")
                    .append(result);

            // Append to file x, y, category
            try {
                bufferedWriter.write(builder.toString() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        bufferedWriter.close();
    }

}

