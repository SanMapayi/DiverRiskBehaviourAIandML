package uk.ac.york.driverriskpredictivemodel.app;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DriverRiskAggregator {

    // Key: driverId|tripId
    private final Map<String, List<Double>> riskMap = new HashMap<>();

    public void addPrediction(String tripId, String driverId, double score) {
        String key = tripId + "|" + driverId;

        riskMap.computeIfAbsent(key, k -> new ArrayList<>()).add(score);
    }

    public void exportToCsv(String filePath) {

        try (FileWriter fw = new FileWriter(filePath)) {

            fw.write("trip_id,driver_id,avg_risk_score,risk_level\n");

            for (String key : riskMap.keySet()) {

                String[] parts = key.split("\\|");
                String tripId = parts[0];
                String driverId = parts[1];

                List<Double> scores = riskMap.get(key);
                double avg = scores.stream()
                        .mapToDouble(d -> d)
                        .average().orElse(0);

                fw.write(driverId + "," + tripId + "," + avg + "," + classify(avg) + "\n");
            }

            System.out.println("✅ Trip-level risk report saved!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String classify(double score) {
        if (score >= 0.7) return "HIGH";
        if (score >= 0.4) return "MEDIUM";
        return "LOW";
    }
}
