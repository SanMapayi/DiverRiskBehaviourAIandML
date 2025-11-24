package uk.ac.york.driverriskpredictivemodel.app;

import uk.ac.york.driverriskpredictivemodel.config.GlobalVariables;
import uk.ac.york.driverriskpredictivemodel.model.*;

public class DemoPredictor {

    public static void main(String[] args) throws Exception {

        ModelSaver saver = new ModelSaver();
        Predictor predictor = new Predictor();

        var model = saver.load(GlobalVariables.DATA_RISK_MODEL.toString());

        double[] sample = {
                50, 1.2, 3.0, 120, 15.0, 0.3, 0.1, 1
        };

        int result = predictor.predict(model, sample);

        System.out.println(result == 1 ? "Risky Driving" : "Safe Driving");
    }
}
