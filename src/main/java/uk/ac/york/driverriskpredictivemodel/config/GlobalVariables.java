package uk.ac.york.driverriskpredictivemodel.config;

import java.nio.file.Path;

public class GlobalVariables {

    public static Path CLEAN_DATA = Path.of("dataset","processed","clean_data.csv");
    public static Path DATA_RISK_MODEL = Path.of("dataset","driver-risk-model.zip");

    public static Path INPUT_RAW_FILE = Path.of("dataset", "raw", "driver_behavior_route_anomaly_dataset_with_derived_features.csv");

    public static Path TRAINFILEPATH =  Path.of("dataset","processed","train_data.csv");
    public static Path TESTFILEPATH  = Path.of("dataset","processed","test_data.csv");

    public static Path RISK_REPORT_PATH = Path.of("docs","experiments","trip_risk_report.csv");

    public static Path RESULTPATH = Path.of("docs","experiments","results.csv");
    public static Path LOSSPATH = Path.of("docs","experiments","loss.csv");

    public static Path CONFUSIONMATRIX_PATH = Path.of("docs","experiments","confusion-matrix.csv");

    public static Path TRAINING_ACCURACY_PNG_PATH = Path.of("docs","experiments","training-accuracy.png");
    public static Path TRAINING_LOSS_PNG_PATH = Path.of("docs","experiments","training-loss.png");


}
