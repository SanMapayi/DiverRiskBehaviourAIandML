package uk.ac.york.driverriskpredictivemodel.model;


import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class Predictor {

    // Existing method – returns class label (0 or 1)
    public int predict(MultiLayerNetwork model, double[] features) {
        INDArray input = Nd4j.create(features).reshape(1, features.length);
        INDArray output = model.output(input);
        return Nd4j.argMax(output, 1).getInt(0);
    }

    // NEW METHOD – returns probability of risky class (class 1)
    public double predictProbability(MultiLayerNetwork model, double[] features) {

        // ✅ Convert to proper 2D shape: [1, featureCount]
        INDArray input = Nd4j.create(features).reshape(1, features.length);

        INDArray output = model.output(input);

        // Return probability of class "1" (risky)
        return output.getDouble(0, 1);
    }

}

