package uk.ac.york.driverriskpredictivemodel.model;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.evaluation.classification.ConfusionMatrix;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

import java.io.FileWriter;
import java.io.IOException;

public class ModelEvaluator {

    public void evaluate(MultiLayerNetwork model,
                         DataSetIterator testIter,
                         String confusionMatrixCsvPath) {

        Evaluation eval = model.evaluate(testIter);

        // Print metrics to console
        System.out.println(eval.stats());

        // Export confusion matrix
        try {
            exportConfusionMatrix(eval, confusionMatrixCsvPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportConfusionMatrix(Evaluation eval, String path) throws IOException {

        ConfusionMatrix<Integer> cm = eval.getConfusionMatrix();

        int actual0_pred0 = (int) cm.getCount(0, 0);
        int actual0_pred1 = (int) cm.getCount(0, 1);
        int actual1_pred0 = (int) cm.getCount(1, 0);
        int actual1_pred1 = (int) cm.getCount(1, 1);

        try (FileWriter fw = new FileWriter(path)) {

            fw.write("Actual/Predicted,Pred_0,Pred_1\n");
            fw.write("Actual_0," + actual0_pred0 + "," + actual0_pred1 + "\n");
            fw.write("Actual_1," + actual1_pred0 + "," + actual1_pred1 + "\n");
        }

        System.out.println("Confusion matrix saved to: " + path);
    }
}
