# Driver Risk Prediction System (MSc Project – University of York)

This project implements a deep-learning based driver risk prediction system using Java and DeepLearning4J (DL4J).

The system predicts whether a driving event is **risky (anomalous)** or **safe** based on vehicle telemetry data.

## Project Structure

driver-risk/
├─ pom.xml
├─ README.md
├─ docs/
│   ├─ DissertationDraft.md
│   ├─ SystemArchitecture.png
│   ├─ DatasetDescription.md
│   └─ Experiments/
│       ├─ hyperparameters.md
│       ├─ results.csv
│       └─ confusion-matrix.png
└─ src/
└─ main/
└─ java/
└─ uk/ac/york/driverriskpredicitivemodel/
├─ data/
│   ├─ DataLoader.java
│   ├─ DataCleaner.java
│   ├─ DataTransformer.java
│   └─ TrainTestSplitter.java
│
├─ model/
│   ├─ NeuralNetworkConfig.java
│   ├─ ModelTrainer.java
│   ├─ ModelEvaluator.java
│   ├─ ModelSaver.java
│   └─ Predictor.java
│
├─ util/
│   ├─ CSVUtils.java
│   ├─ NormalisationUtils.java
│   └─ PlotUtils.java
    └─ ChartGenerator.java
│
└─ app/
    ├─ Main.java
    └─ DemoPredictor.java
    └─ BatchPredictor.java
    └─ DriverRiskAggregator.java


## Features Used
- speed
- acceleration
- steering angle
- trip duration
- trip distance
- brake usage
- lane deviation
- weather conditions

## Label
The prediction target is: anomalous_event (0 = Safe, 1 = Risky)

## How to Run
    Raw Dataset
        ↓
    DataCleaner
        ↓
    clean_data.csv
        ↓
    Main (Training + Evaluation)
        ↓
    driver-risk-model.zip
        results.csv
        loss.csv
    confusion-matrix.csv
        ↓
    ChartGenerator (optional)
        ↓
    training-accuracy.png
    training-loss.png

If using Maven from terminal
mvn clean compile



# Load raw data, clean data, split to test and train data, Train model
mvn exec:java -Dexec.mainClass="uk.ac.york.driverriskpredictivemodel.app.Main"

# Charts
mvn exec:java -Dexec.mainClass="uk.ac.york.driverriskpredictivemodel.util.ChartGenerator"

# Single prediction
mvn exec:java -Dexec.mainClass="uk.ac.york.driverriskpredictivemodel.app.DemoPredictor"

# Batch prediction
mvn exec:java -Dexec.mainClass="uk.ac.york.driverriskpredictivemodel.app.BatchPredictor"


Technologies
    Java 23
    Deeplearning4J
    DataVec
    ND4J
    Maven


