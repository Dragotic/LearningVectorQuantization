package lvq;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class LVQ extends JFrame {

    private static final int M            = 7;
    private static final int epochs       = 5;
    private static double learningRate    = 0.1;

    private static double totalDispersion         = 0.0;
    private static double previousTotalDispersion = 0.0;

    private static ArrayList<Point> points              = new ArrayList<>();
    private static ArrayList<CompetitiveNeuron> centers = new ArrayList<>();
    private static ArrayList<ArrayList<Point>> clusters = new ArrayList<>();
    private static ArrayList<Double> dispersions        = new ArrayList<>();

    private static ArrayList<CompetitiveNeuron> previousCenters = new ArrayList<>();
    private static ArrayList<ArrayList<Point>> previousClusters = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = 1; i < 6; i++) {
            System.out.println("Now running for iteration #"  + i);
            readPointsFromFile();
            randomCenters();
            train();
            predict();
            totalDispersion();

            for (Double dispersion: dispersions) {
                totalDispersion += dispersion;
            }

            for (double dis: dispersions) {
                System.out.println(dis);
            }
            System.out.println("Total Dispersion for this iteration=" + totalDispersion);

            if (previousTotalDispersion == 0.0 || previousTotalDispersion > totalDispersion) {
                previousTotalDispersion = totalDispersion;

                previousCenters.clear();
                previousClusters.clear();

                for (CompetitiveNeuron neuron: centers) {
                    previousCenters.add(neuron);
                }

                for (ArrayList<Point> cluster: clusters) {
                    previousClusters.add(cluster);
                }
            }

            clusters.clear();
            dispersions.clear();
            points.clear();
            centers.clear();
            totalDispersion = 0.0;


        }
        LVQ example = new LVQ("Scatter Chart Example");
        example.setSize(800, 400);
        example.setLocationRelativeTo(null);
        example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        example.setVisible(true);

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

    private static void randomCenters() {
        for (int i = 0; i < M; i++) {
            Point p = points.remove(ThreadLocalRandom.current().nextInt(0, points.size()));
            CompetitiveNeuron center = new CompetitiveNeuron(p, p, i);
            centers.add(center);
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
        return centers.get(winnerNeuron);
    }

    private static void train() {
        for (int i = 0; i < epochs; i++) {
            for (Point p: points) {
                CompetitiveNeuron winner = bestMatchingUnit(p);
                int indx = centers.indexOf(winner);

                Point oldWeight = winner.getWeight();
                double newX = oldWeight.getX() + learningRate * (p.getX() - oldWeight.getX());
                double newY = oldWeight.getY() + learningRate * (p.getY() - oldWeight.getY());

                Point newWeight = new Point(newX, newY);
                centers.get(indx).setWeight(newWeight);
            }

            learningRate = learningRate * (1.0 - (i/epochs));
        }
    }

    private static void predict() {
        for (int i = 0; i < M; i++) {
            ArrayList<Point> cluster = new ArrayList<>();
            clusters.add(cluster);
        }

        for (Point p: points) {
            CompetitiveNeuron winner = bestMatchingUnit(p);
            clusters.get(winner.getTeam()).add(p);
        }

    }

    private static void totalDispersion() {
        for (int i = 0; i < clusters.size(); i++) {
            double dispersion = 0.0;
            ArrayList<Point> cluster = clusters.get(i);
            CompetitiveNeuron center = centers.get(i);

            for (Point p: cluster) {
                dispersion += euclideanDistance(p, center.getWeight());
            }
            dispersions.add(dispersion);
        }
    }

    private XYDataset createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries series = new XYSeries("Centers");

        for (CompetitiveNeuron point : previousCenters) {
            series.add(point.getWeight().getX(), point.getWeight().getY());
        }

        dataset.addSeries(series);

        int i=0;
        for (ArrayList<Point> cluster : previousClusters) {
            series = new XYSeries("Cluster " + i);
            for (Point point : cluster) {
                series.add(point.getX(), point.getY());
            }
            i++;
            dataset.addSeries(series);
        }

        return dataset;
    }

    public LVQ(String title) {
        super(title);

        // Create dataset
        XYDataset dataset = createDataset();

        // Create chart
        JFreeChart chart = ChartFactory.createScatterPlot("All the points",
                "X-Axis",
                "Y-Axis",
                dataset, PlotOrientation.VERTICAL,
                true, true, false);


        //Changes background color
        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(new Color(255,228,196));


        // Create Panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

}
