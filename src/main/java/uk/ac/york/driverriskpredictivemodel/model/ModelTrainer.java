package uk.ac.york.driverriskpredictivemodel.model;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import uk.ac.york.driverriskpredictivemodel.config.GlobalVariables;
import uk.ac.york.driverriskpredictivemodel.util.PlotUtils;

import java.io.IOException;

public class ModelTrainer {

    String resultsFile = GlobalVariables.RESULTPATH.toString();
    String lossFile = GlobalVariables.LOSSPATH.toString();
    public void train(MultiLayerNetwork model,
                      DataSetIterator train,
                      DataSetIterator test,
                      int epochs) throws IOException {

        for (int epoch = 1; epoch <= epochs; epoch++) {

            model.fit(train);
            train.reset();

            Evaluation eval = model.evaluate(test);
            test.reset();

            double acc = eval.accuracy();
            double precision = eval.precision(1);
            double recall = eval.recall(1);
            double f1 = eval.f1(1);

            double loss = model.score();

            System.out.printf(
                    "Epoch %d | Acc: %.4f | Prec: %.4f | Recall: %.4f | F1: %.4f | Loss: %.4f%n",
                    epoch, acc, precision, recall, f1, loss
            );

            // ✅ Save metrics for plots
            PlotUtils.logMetric(resultsFile, epoch, acc);
            PlotUtils.logMetric(lossFile, epoch, loss);
        }
    }
}
