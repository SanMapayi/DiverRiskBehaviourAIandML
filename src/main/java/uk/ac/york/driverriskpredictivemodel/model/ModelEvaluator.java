package uk.ac.york.driverriskpredictivemodel.model;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

import java.io.FileWriter;
import java.util.List;

public class ModelEvaluator {

    public void evaluate(MultiLayerNetwork model,
                         DataSetIterator test,
                         String runFolder) {

        try {
            Evaluation eval = model.evaluate(test);
            test.reset();

            // DL4J legacy confusion matrix
            var cm = eval.getConfusionMatrix();

            // Extract class labels
            List<Integer> actualClasses = cm.getClasses();

            int class0 = actualClasses.get(0);
            int class1 = actualClasses.get(1);

            int actual0_pred0 = cm.getCount(class0, class0);
            int actual0_pred1 = cm.getCount(class0, class1);
            int actual1_pred0 = cm.getCount(class1, class0);
            int actual1_pred1 = cm.getCount(class1, class1);

            String file = runFolder + "/confusion-matrix.csv";

            try (FileWriter fw = new FileWriter(file)) {

                fw.write("Actual/Predicted,Pred_0,Pred_1\n");
                fw.write("Actual_0," + actual0_pred0 + "," + actual0_pred1 + "\n");
                fw.write("Actual_1," + actual1_pred0 + "," + actual1_pred1 + "\n");
            }

            System.out.println("✅ Confusion Matrix saved: " + file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
