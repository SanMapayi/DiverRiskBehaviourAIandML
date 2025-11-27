package uk.ac.york.driverriskpredictivemodel.model;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import uk.ac.york.driverriskpredictivemodel.config.GlobalVariables;
import uk.ac.york.driverriskpredictivemodel.util.PlotUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ModelTrainer {

    String resultsFile = GlobalVariables.RESULTPATH.toString();
    String lossFile = GlobalVariables.LOSSPATH.toString();
    private final String runFolder;

    public ModelTrainer() {
        String time =
                java.time.LocalDateTime.now().toString().replace(":", "-");

        this.runFolder = "docs/Experiments/run_" + time;
        new File(runFolder).mkdirs();
    }

    public String getRunFolder() {
        return runFolder;
    }

    public void train(MultiLayerNetwork model,
                      DataSetIterator train,
                      DataSetIterator test,
                      int epochs) throws IOException {

        String resultsFile = runFolder + "/results.csv";
        String lossFile    = runFolder + "/loss.csv";

        try (FileWriter fw = new FileWriter(resultsFile)) {
            fw.write("epoch,accuracy,precision,recall,f1\n");
        } catch (Exception ignored) {}

        try (FileWriter fw = new FileWriter(lossFile)) {
            fw.write("epoch,loss\n");
        } catch (Exception ignored) {}

        for (int epoch = 1; epoch <= epochs; epoch++) {

            model.fit(train);
            train.reset();

            Evaluation eval = model.evaluate(test);
            test.reset();

            double acc = eval.accuracy();
            double prec = eval.precision(1);
            double rec = eval.recall(1);
            double f1 = eval.f1(1);
            double loss = model.score();

            System.out.printf("Epoch %d → Acc=%.3f Prec=%.3f Recall=%.3f F1=%.3f%n",
                    epoch, acc, prec, rec, f1);

            PlotUtils.logMetric(resultsFile, epoch, acc, prec, rec, f1);
            PlotUtils.logMetric(lossFile, epoch, loss);
        }
    }
}
