package uk.ac.york.driverriskpredictivemodel.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import uk.ac.york.driverriskpredictivemodel.config.GlobalVariables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ChartGenerator {

    public static void main(String[] args) throws Exception {

        generateAccuracyChart();
        generateLossChart();
    }

    private static void generateAccuracyChart() throws Exception {

        String csvFile = GlobalVariables.RESULTPATH.toString();
        String outputImage = GlobalVariables.TRAINING_ACCURACY_PNG_PATH.toString();

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
        System.out.println("aAccuracy chart saved: " + outputImage);
    }

    private static void generateLossChart() throws Exception {

        String csvFile = GlobalVariables.LOSSPATH.toString();
        String outputImage = GlobalVariables.TRAINING_LOSS_PNG_PATH.toString();

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
        System.out.println("Loss chart saved: " + outputImage);
    }
}
