package uk.ac.york.driverriskpredictivemodel.data;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.records.reader.impl.transform.TransformProcessRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.transform.TransformProcess;
import org.datavec.api.transform.schema.Schema;

import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;

import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import uk.ac.york.driverriskpredictivemodel.config.GlobalVariables;

import java.io.File;

public class DataTransformer {

    private static final int BATCH_SIZE = 64;
    private static final int NUM_CLASSES = 2;

    public static class Result {
        public DataSetIterator trainIterator;
        public DataSetIterator testIterator;

        public Result(DataSetIterator train, DataSetIterator test) {
            this.trainIterator = train;
            this.testIterator = test;
        }
    }

    public Result buildIterators() throws Exception {

        // -----------------------
        // Define Schema
        // -----------------------
        Schema schema = new Schema.Builder()
                .addColumnString("trip_id")      // index 0
                .addColumnString("driver_id")        // index 1
                .addColumnDouble("speed")          // index 2
                .addColumnDouble("acceleration")   // index 3
                .addColumnDouble("steering_angle") // index 4
                .addColumnDouble("trip_duration")  // index 5
                .addColumnDouble("trip_distance")  // index 6
                .addColumnDouble("brake_usage")    // index 7
                .addColumnDouble("cornering")      // index 8
                .addColumnDouble("weather")        // index 9
                .addColumnInteger("label")         // index 10
                .build();

        // Remove metadata columns
        TransformProcess tp = new TransformProcess.Builder(schema)
                .removeColumns("trip_id", "driver_id")
                .build();

        // -----------------------
        // Train reader
        // -----------------------
        RecordReader trainRR = new CSVRecordReader(1, ',');
        trainRR.initialize(new FileSplit(new File(GlobalVariables.TRAINFILEPATH.toString())));

        RecordReader trainReader =
                new TransformProcessRecordReader(trainRR, tp);

        // -----------------------
        // Test reader
        // -----------------------
        RecordReader testRR = new CSVRecordReader(1, ',');
        testRR.initialize(new FileSplit(new File(GlobalVariables.TESTFILEPATH.toString())));

        RecordReader testReader =
                new TransformProcessRecordReader(testRR, tp);

        // After removing 2 columns:
        // features = 8 cols (0–7), label = index 8
        int labelIndex = 8;

        DataSetIterator trainIter =
                new RecordReaderDataSetIterator(trainReader, BATCH_SIZE, labelIndex, NUM_CLASSES);

        DataSetIterator testIter =
                new RecordReaderDataSetIterator(testReader, BATCH_SIZE, labelIndex, NUM_CLASSES);

        // -----------------------
        // Normalisation
        // -----------------------
        DataNormalization normalizer = new NormalizerStandardize();
        normalizer.fit(trainIter);

        trainIter.setPreProcessor(normalizer);
        testIter.setPreProcessor(normalizer);

        trainIter.reset();
        testIter.reset();

        return new Result(trainIter, testIter);
    }
}
