package lvq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Map.Entry.*;
import static java.util.stream.Collectors.*;

public class LVQ {

    private static final int M = 5;
    private static ArrayList<Point> points = new ArrayList<>();
    private static ArrayList<CompetitiveNeuron> centers = new ArrayList<>();
    private static Map<Point, Double> codebooks = new HashMap<>();
    private static double learningRate = 0.3;
    private static int epochs = 5;

    public static void main(String[] args) {
        readPointsFromFile();
        Point row = points.get(0);
//        for (Point p : points) {
//            System.out.println(bestMatchingUnit(p) + "-----");
//        }

        for (int i = 0; i < M; i++) {
            Point p = points.remove(ThreadLocalRandom.current().nextInt(0, points.size()));
            CompetitiveNeuron center = new CompetitiveNeuron(p, p);
            centers.add(center);
        }

        System.out.println("---------- OLD Neurons ----------");
        for (CompetitiveNeuron center: centers) {
            System.out.println(center);
        }

        train();

        System.out.println("---------- NEW Neurons ----------");
        for (CompetitiveNeuron center: centers) {
            System.out.println(center);
        }

    }

    private static void readPointsFromFile() {
        File file = new File("clustering_data.txt");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String sCurrentLine;
            while ((sCurrentLine = bufferedReader.readLine()) != null) {
                Point point = new Point(Double.valueOf(sCurrentLine.split(",")[0]), Double.valueOf(sCurrentLine.split(",")[1]));
                points.add(point);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double euclideanDistance(Point row1, Point row2) {

        Double distance = Math.pow((row1.getX() - row2.getX()), 2) + Math.pow(row1.getY() - row2.getY(), 2);

        return Math.sqrt(distance);
    }

    private static CompetitiveNeuron bestMatchingUnit(Point row) {
        ArrayList<Double> distances = new ArrayList<>();

        for (CompetitiveNeuron center: centers) {
            distances.add(euclideanDistance(row, center.getWeight()));
        }

        int winnerNeuron = distances.indexOf(Collections.min(distances));
        return centers.remove(winnerNeuron);
    }

    private static void train() {
        for (int i = 0; i < epochs; i++) {
            System.out.println("Epoch: " + i);
            for (Point p: points) {
                CompetitiveNeuron winner = bestMatchingUnit(p);
                Point oldWeight = winner.getWeight();
                double newX = oldWeight.getX() + learningRate * (p.getX() - oldWeight.getX());
                double newY = oldWeight.getY() + learningRate * (p.getY() - oldWeight.getY());

                Point newWeight = new Point(newX, newY);
                winner.setWeight(newWeight);
                centers.add(winner);
            }

            learningRate = learningRate * (1.0 - (i/epochs));
        }
    }

//    private static Map.Entry<Point, Double> bestMatchingUnit(Point row) {
//        Map<Point, Double> distances = new HashMap<>();
//        Double distance = 0.0;
//        points.forEach(point -> {
//            if (!row.equals(point)) {
//                Double dist = euclideanDistance(point, row);
//                distances.put(point, dist);
//            }
//        });
//
//        Map<Point, Double> sorted = distances
//                .entrySet()
//                .stream()
//                .sorted(comparingByValue())
//                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
//                                LinkedHashMap::new));
//
//        Map.Entry<Point,Double> entry = sorted.entrySet().iterator().next();
//        System.out.println(entry.getKey() + "   " + entry.getValue());
//        return entry;
//    }

//    private static Map<Point, Double> train() {
//        for (int i = 0; i < epochs; i++) {
//            double lrate = learningRate * (1.0 - (i/epochs));
//            double sum_error = 0.0;
//
//            codebooks.forEach((key, value) -> {
//                Map.Entry<Point, Double> bmu = bestMatchingUnit(key);
//                double error = key.getX() - bmu.getKey().getX();
//                sum_error += Math.pow(error, 2);
//            });
//
//        }
//    }
}
