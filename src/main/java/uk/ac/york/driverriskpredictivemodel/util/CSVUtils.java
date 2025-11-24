package uk.ac.york.driverriskpredictivemodel.util;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;

public class CSVUtils {

    public static void writeRow(String path, String[] row) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(path, true))) {
            writer.writeNext(row);
        }
    }
}
