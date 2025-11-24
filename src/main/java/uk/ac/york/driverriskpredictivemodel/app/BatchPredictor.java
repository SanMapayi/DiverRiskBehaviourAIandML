package uk.ac.york.driverriskpredictivemodel.app;

import com.opencsv.CSVReader;
import uk.ac.york.driverriskpredictivemodel.config.GlobalVariables;
import uk.ac.york.driverriskpredictivemodel.model.ModelSaver;
import uk.ac.york.driverriskpredictivemodel.model.Predictor;

import java.io.FileReader;
import java.util.Arrays;

public class BatchPredictor {

    public static void main(String[] args) throws Exception {

        ModelSaver saver = new ModelSaver();
        Predictor predictor = new Predictor();
        DriverRiskAggregator aggregator = new DriverRiskAggregator();

        var model = saver.load(GlobalVariables.DATA_RISK_MODEL.toString());

        CSVReader reader = new CSVReader(new FileReader(GlobalVariables.CLEAN_DATA.toString()));

        String[] header = reader.readNext();
        String[] row;

        int total = 0;
        int failed = 0;

        while ((row = reader.readNext()) != null) {

            try {
                if (row.length < 10) {
                    System.out.println("⚠ Skipping short row: " + Arrays.toString(row));
                    failed++;
                    continue;
                }

                String driverId = row[0];
                String tripId   = row[1];

                double speed         = Double.parseDouble(row[2]);
                double acceleration  = Double.parseDouble(row[3]);
                double steering      = Double.parseDouble(row[4]);
                double duration      = Double.parseDouble(row[5]);
                double distance      = Double.parseDouble(row[6]);
                double brake         = Double.parseDouble(row[7]);
                double cornering     = Double.parseDouble(row[8]);
                double weather       = Double.parseDouble(row[9]);

                double[] features = {
                        speed, acceleration, steering,
                        duration, distance, brake,
                        cornering, weather
                };

                System.out.println("✅ Features: " + Arrays.toString(features));

                double risk = predictor.predictProbability(model, features);
                System.out.println("✅ Risk score: " + risk);

                aggregator.addPrediction(tripId, driverId, risk);

                total++;

            } catch (Exception e) {
                System.out.println("❌ Error processing row: " + Arrays.toString(row));
                e.printStackTrace();
                failed++;
            }
        }

        reader.close();

        System.out.println("✅ Total rows processed: " + total);
        System.out.println("❌ Total failed rows: " + failed);

        aggregator.exportToCsv(GlobalVariables.RISK_REPORT_PATH.toString());
    }
}
