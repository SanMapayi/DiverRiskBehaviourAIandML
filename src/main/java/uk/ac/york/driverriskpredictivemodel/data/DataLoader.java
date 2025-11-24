package uk.ac.york.driverriskpredictivemodel.data;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import uk.ac.york.driverriskpredictivemodel.config.GlobalVariables;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class DataLoader {


    private List<String[]> loadCSV(String filePath) throws IOException, CsvException {
        System.out.println("Loading dataset: " + filePath);

        CSVReader reader = new CSVReader(new FileReader(filePath));
        List<String[]> rows = reader.readAll();
        reader.close();

        if (rows.isEmpty()) {
            throw new RuntimeException("CSV file is empty: " + filePath);
        }

        validateColumns(rows.get(0));

        System.out.println("CSV Loaded. Total rows: " + (rows.size() - 1));
        return rows;
    }

    private void validateColumns(String[] header) {
        String[] required = { "trip_id", "driver_id",
                "speed", "acceleration", "steering_angle",
                "trip_duration", "trip_distance", "brake_usage",
                "lane_deviation", "weather_conditions",
                "anomalous_event"
        };

        for (String col : required) {
            boolean found = false;
            for (String h : header) {
                if (h.equalsIgnoreCase(col)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new RuntimeException("Missing required column: " + col);
            }
        }

        System.out.println("Header validation passed.");
    }


    public void loadCSV() throws IOException, CsvException {
        loadCSV(GlobalVariables.INPUT_RAW_FILE.toString());
    }
}
