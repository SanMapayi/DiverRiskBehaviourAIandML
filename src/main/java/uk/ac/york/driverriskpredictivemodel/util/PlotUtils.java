package uk.ac.york.driverriskpredictivemodel.util;

import java.io.FileWriter;
import java.io.IOException;

public class PlotUtils {

    public static void logMetric(String path, int epoch, double... values)
            throws IOException {

        try (FileWriter fw = new FileWriter(path, true)) {
            fw.write(epoch + "");

            for (double v : values) {
                fw.write("," + v);
            }
            fw.write("\n");
        }
    }
}
