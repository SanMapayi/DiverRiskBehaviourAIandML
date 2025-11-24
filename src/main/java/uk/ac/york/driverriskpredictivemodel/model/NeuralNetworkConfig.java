package uk.ac.york.driverriskpredictivemodel.model;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.impl.LossMCXENT;

public class NeuralNetworkConfig {

    public static MultiLayerNetwork build(int inputSize, int numClasses) {

        // ✅ Give higher weight to risky class
        INDArray classWeights = Nd4j.create(new double[]{
                1.0,  // Class 0 - Safe
                8.0   // Class 1 - Risky (penalise mistakes more)
        });

        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
                .updater(new Adam(0.001))
                .list()
                .layer(new DenseLayer.Builder()
                        .nIn(inputSize)
                        .nOut(32)
                        .activation(Activation.RELU)
                        .build())
                .layer(new DenseLayer.Builder()
                        .nOut(16)
                        .activation(Activation.RELU)
                        .build())
                .layer(new OutputLayer.Builder()
                        .lossFunction(new LossMCXENT(classWeights))  // Correct approach
                        .activation(Activation.SOFTMAX)
                        .nOut(numClasses)
                        .build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(config);
        model.init();

        return model;
    }
}