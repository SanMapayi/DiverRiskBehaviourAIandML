package uk.ac.york.driverriskpredictivemodel.data;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import uk.ac.york.driverriskpredictivemodel.config.GlobalVariables;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class DataCleaner {

    public void cleanData() {

        Path inputFile = GlobalVariables.INPUT_RAW_FILE;
        Path outputFile = GlobalVariables.CLEAN_DATA;

        try (
                CSVReader reader = new CSVReader(new FileReader(inputFile.toString()));
                CSVWriter writer = new CSVWriter(new FileWriter(outputFile.toString()))
        ) {
            String[] header = reader.readNext();

            // Map header → index
            Map<String, Integer> col = mapColumns(header);

            // Output header (the selected ML fields + label)
            String[] outputHeader = { "trip_id", "driver_id",
                    "speed", "acceleration", "steering_angle",
                    "trip_duration", "trip_distance", "brake_usage",
                    "lane_deviation", "weather_numeric",
                    "label"
            };

            writer.writeNext(outputHeader);

            String[] row;

            while ((row = reader.readNext()) != null) {

                // Extract inputs
                String tripId = row[col.get("trip_id")];
                String driverId = row[col.get("driver_id")];
                String speed = row[col.get("speed")];
                String acceleration = row[col.get("acceleration")];
                String steering = row[col.get("steering_angle")];
                String duration = row[col.get("trip_duration")];
                String distance = row[col.get("trip_distance")];
                String brake = row[col.get("brake_usage")];
                String lane = row[col.get("lane_deviation")];
                String weather = row[col.get("weather_conditions")];

                // Label column
                String label = row[col.get("anomalous_event")];

                // Skip missing
                if (anyEmpty(tripId, driverId, speed, acceleration, steering, duration, distance, brake, lane, weather, label))
                    continue;

                // Validate numeric
                if (!( isNumeric(tripId) && isNumeric(driverId) && isNumeric(speed) && isNumeric(acceleration) && isNumeric(steering)
                        && isNumeric(duration) && isNumeric(distance) && isNumeric(brake)
                        && isNumeric(lane) && isNumeric(label)))
                    continue;

                // Convert weather
                String weatherNumeric = convertWeather(weather);

                // Build cleaned row
                String[] cleanedRow = { tripId, driverId,
                        speed, acceleration, steering, duration,
                        distance, brake, lane, weatherNumeric, label
                };

                writer.writeNext(cleanedRow);
            }

            System.out.println("Cleaning complete → " + outputFile);

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Integer> mapColumns(String[] header) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < header.length; i++) {
            map.put(header[i], i);
        }
        return map;
    }

    private boolean anyEmpty(String... vals) {
        for (String v : vals) {
            if (v == null || v.isEmpty()) return true;
        }
        return false;
    }

    private boolean isNumeric(String v) {
        try {
            Double.parseDouble(v);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String convertWeather(String weather) {
        weather = weather.toLowerCase();
        return switch (weather) {
            case "sunny" -> "0";
            case "rainy" -> "1";
            case "snow", "snowy" -> "2";
            case "fog", "foggy" -> "3";
            default -> "0"; // fallback category
        };
    }
}
