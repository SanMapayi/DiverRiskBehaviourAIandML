package uk.ac.york.driverriskpredictivemodel.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.*;

public class ExperimentChart {

    public static void main(String[] args) throws Exception {

        String time = java.time.LocalDateTime.now()
                .toString()
                .replace(":", "-");

        String baseFolder = "docs/Experiments/comparison";
        String latestFolder = baseFolder + "/latest";
        String historyFolder = baseFolder + "/history";

        new File(latestFolder).mkdirs();
        new File(historyFolder).mkdirs();

        String csvFile = latestFolder + "/experiment-comparison.csv";

        String latestImage = latestFolder + "/experiment-comparison.png";
        String historyImage = historyFolder + "/experiment-comparison_" + time + ".png";

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        br.readLine(); // skip header

        String line;
        while ((line = br.readLine()) != null) {

            String[] parts = line.split(",");

            if (parts.length < 3) continue;

            String run = parts[0];
            double safe = Double.parseDouble(parts[1]);
            double risky = Double.parseDouble(parts[2]);

            dataset.addValue(safe, "Safe Correct", run);
            dataset.addValue(risky, "Risky Correct", run);
        }
        br.close();

        JFreeChart chart = ChartFactory.createLineChart(
                "Model Performance Across Runs",
                "Run",
                "Correct Predictions",
                dataset
        );

        ChartUtils.saveChartAsPNG(new File(latestImage), chart, 900, 600);
        ChartUtils.saveChartAsPNG(new File(historyImage), chart, 900, 600);

        System.out.println("✅ Experiment comparison charts saved:");
        System.out.println(latestImage);
        System.out.println(historyImage);
    }
}
