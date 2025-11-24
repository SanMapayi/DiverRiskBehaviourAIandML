package uk.ac.york.driverriskpredictivemodel.app;

import com.opencsv.exceptions.CsvException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import uk.ac.york.driverriskpredictivemodel.config.GlobalVariables;
import uk.ac.york.driverriskpredictivemodel.data.DataCleaner;
import uk.ac.york.driverriskpredictivemodel.data.DataLoader;
import uk.ac.york.driverriskpredictivemodel.data.DataTransformer;
import uk.ac.york.driverriskpredictivemodel.data.TrainTestSplitter;
import uk.ac.york.driverriskpredictivemodel.model.ModelEvaluator;
import uk.ac.york.driverriskpredictivemodel.model.ModelSaver;
import uk.ac.york.driverriskpredictivemodel.model.ModelTrainer;
import uk.ac.york.driverriskpredictivemodel.model.NeuralNetworkConfig;
import uk.ac.york.driverriskpredictivemodel.util.ConfusionMatrixChart;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class main {

    public static void main(String[] args) throws Exception {


        // -----------------------
        // Load Raw data if the clean_data does not exist,
        // and verify the required fields for training are present
        // -----------------------
        if (!Files.exists(GlobalVariables.CLEAN_DATA)) {
            DataLoader dataLoader = new DataLoader();
            DataCleaner dataCleaner = new DataCleaner();
            try {
                dataLoader.loadCSV();
                dataCleaner.cleanData();
            } catch (IOException | CsvException e) {
                throw new RuntimeException(e);
            }
        }

        if (!(Files.exists(GlobalVariables.TESTFILEPATH)
                && Files.exists(GlobalVariables.TRAINFILEPATH))) {
            TrainTestSplitter.splitDataToTestAndTraining();
        }

        // Load data
        DataTransformer dt = new DataTransformer();
        var data = dt.buildIterators();

        ModelSaver saver = new ModelSaver();
        MultiLayerNetwork model;

        File savedModel = new File(GlobalVariables.DATA_RISK_MODEL.toString());

        if (savedModel.exists()) {
            model = saver.load(savedModel.getPath());
            System.out.println("Loaded existing model");
        } else {
            model = NeuralNetworkConfig.build(8, 2);
            System.out.println("Created new model");
        }

        // Train
        ModelTrainer trainer = new ModelTrainer();
        trainer.train(model, data.trainIterator, data.testIterator, 10);

        // Evaluate
        ModelEvaluator evaluator = new ModelEvaluator();
        evaluator.evaluate(model, data.testIterator, GlobalVariables.CONFUSIONMATRIX_PATH.toString());
        ConfusionMatrixChart.main(null);

        // Save model
        saver.save(model, savedModel.getPath());
        System.out.println("Model saved");
    }


}

