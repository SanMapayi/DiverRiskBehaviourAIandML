package uk.ac.york.driverriskpredictivemodel.util;

import java.io.FileWriter;
import java.io.IOException;

public class PlotUtils {

    public static void logMetric(String path, int epoch, double value) throws IOException {
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(epoch + "," + value + "\n");
        }
    }
}
