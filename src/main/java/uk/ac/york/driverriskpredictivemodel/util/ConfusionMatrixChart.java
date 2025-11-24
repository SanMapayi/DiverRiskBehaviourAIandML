package uk.ac.york.driverriskpredictivemodel.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.*;

public class ConfusionMatrixChart {

    public static void main(String[] args) throws Exception {

        String csvFile = "docs/experiments/confusion-matrix.csv";
        String pngFile = "docs/experiments/confusion-matrix.png";
        String pdfFile = "docs/experiments/confusion-matrix.pdf";

        int tn = 0, fp = 0, fn = 0, tp = 0;

        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        br.readLine(); // Skip header

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");

            String label = parts[0];
            int pred0 = Integer.parseInt(parts[1]);
            int pred1 = Integer.parseInt(parts[2]);

            if (label.equals("Actual_0")) {
                tn = pred0;
                fp = pred1;
            } else if (label.equals("Actual_1")) {
                fn = pred0;
                tp = pred1;
            }
        }
        br.close();

        // Metrics
        double precision = tp / (double) (tp + fp);
        double recall = tp / (double) (tp + fn);

        // Build dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(tn, "Pred_0", "Actual_0");
        dataset.addValue(fp, "Pred_1", "Actual_0");
        dataset.addValue(fn, "Pred_0", "Actual_1");
        dataset.addValue(tp, "Pred_1", "Actual_1");

        JFreeChart chart = ChartFactory.createBarChart(
                "Confusion Matrix",
                "Actual Class",
                "Count",
                dataset
        );

        // Custom colouring by magnitude
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int col) {
                Number value = dataset.getValue(row, col);
                int v = value != null ? value.intValue() : 0;

                if (v > 5000) return new Color(0, 100, 0);
                if (v > 2000) return new Color(0, 150, 0);
                if (v > 500)  return new Color(255, 140, 0);
                return new Color(200, 0, 0);
            }
        };

        plot.setRenderer(renderer);

        // Metrics overlay
        chart.addSubtitle(new org.jfree.chart.title.TextTitle(
                String.format("Precision: %.3f | Recall: %.3f", precision, recall)
        ));

        // Save PNG
        ChartUtils.saveChartAsPNG(new File(pngFile), chart, 900, 700);
        System.out.println("✅ PNG saved: " + pngFile);

        // Save PDF
        exportPNGtoPDF(pngFile, pdfFile);
    }

    // Convert PNG to PDF
    private static void exportPNGtoPDF(String pngPath, String pdfPath) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();

            Image img = Image.getInstance(pngPath);
            img.scaleToFit(500, 500);
            document.add(img);

            document.close();
            System.out.println("✅ PDF saved: " + pdfPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
