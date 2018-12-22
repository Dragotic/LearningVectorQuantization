package lvq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.util.Map.Entry.*;
import static java.util.stream.Collectors.*;

public class LVQ {

    private static ArrayList<Point> points = new ArrayList<>();

    public static void main(String[] args) {
        readPointsFromFile();
        Point row = points.get(0);
        for (Point p : points) {
            System.out.println(bestMatchingUnit(p) + "-----");
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

    private static Double euclideanDistance(Point row1, Point row2) {

        Double distance = Math.pow((row1.getX() - row2.getX()), 2) + Math.pow(row1.getY() - row2.getY(), 2);

        return Math.sqrt(distance);
    }

    private static Double bestMatchingUnit(Point row) {
        Map<Point, Double> distances = new HashMap<>();
        Double distance = 0.0;
        points.forEach(point -> {
            if (!row.equals(point)) {
                Double dist = euclideanDistance(point, row);
                distances.put(point, dist);
            }
        });

        Map<Point, Double> sorted = distances
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

        Map.Entry<Point,Double> entry = sorted.entrySet().iterator().next();
        System.out.println(entry.getKey() + "   " + entry.getValue());
        return entry.getValue();
    }
}
