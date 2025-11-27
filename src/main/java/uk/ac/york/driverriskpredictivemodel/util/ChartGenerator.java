package uk.ac.york.driverriskpredictivemodel.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

public class ChartGenerator {

    public static void main(String[] args) throws Exception {

        File latestRun = findLatestRunFolder();

        if (latestRun == null) {
            System.out.println("❌ No experiment runs found!");
            return;
        }

        generateAccuracyChart(latestRun);
        generateLossChart(latestRun);
    }

    // ✅ Find latest run folder
    private static File findLatestRunFolder() {
        File base = new File("docs/Experiments");

        File[] runs = base.listFiles((dir, name) -> name.startsWith("run_"));

        if (runs == null || runs.length == 0) return null;

        Arrays.sort(runs, Comparator.comparing(File::getName).reversed());

        return runs[0];
    }

    private static void generateAccuracyChart(File runFolder) throws Exception {

        String csvFile = new File(runFolder, "results.csv").getAbsolutePath();
        String outputImage = new File(runFolder, "training-accuracy.png").getAbsolutePath();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        String line = br.readLine(); // skip header

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");

            String epoch = parts[0];
            double accuracy = Double.parseDouble(parts[1]);

            dataset.addValue(accuracy, "Accuracy", epoch);
        }
        br.close();

        JFreeChart chart = ChartFactory.createLineChart(
                "Training Accuracy",
                "Epoch",
                "Accuracy",
                dataset
        );

        ChartUtils.saveChartAsPNG(new File(outputImage), chart, 800, 600);
        System.out.println("✅ Accuracy chart saved: " + outputImage);
    }

    private static void generateLossChart(File runFolder) throws Exception {

        String csvFile = new File(runFolder, "loss.csv").getAbsolutePath();
        String outputImage = new File(runFolder, "training-loss.png").getAbsolutePath();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        String line = br.readLine(); // skip header

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");

            String epoch = parts[0];
            double loss = Double.parseDouble(parts[1]);

            dataset.addValue(loss, "Loss", epoch);
        }
        br.close();

        JFreeChart chart = ChartFactory.createLineChart(
                "Training Loss",
                "Epoch",
                "Loss",
                dataset
        );

        ChartUtils.saveChartAsPNG(new File(outputImage), chart, 800, 600);
        System.out.println("✅ Loss chart saved: " + outputImage);
    }
}
