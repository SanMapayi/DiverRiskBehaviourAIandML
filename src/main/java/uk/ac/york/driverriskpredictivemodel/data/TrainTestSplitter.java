package uk.ac.york.driverriskpredictivemodel.data;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import uk.ac.york.driverriskpredictivemodel.config.GlobalVariables;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainTestSplitter {

    public static void splitDataToTestAndTraining() throws Exception {


        String trainFile = GlobalVariables.TRAINFILEPATH.toString();
        String testFile  = GlobalVariables.TESTFILEPATH.toString();

        double trainRatio = 0.8;

        CSVReader reader = new CSVReader(new FileReader(GlobalVariables.CLEAN_DATA.toFile()));
        CSVWriter trainWriter = new CSVWriter(new FileWriter(trainFile));
        CSVWriter testWriter  = new CSVWriter(new FileWriter(testFile));

        List<String[]> rows = new ArrayList<>();

        String[] header = reader.readNext();
        trainWriter.writeNext(header);
        testWriter.writeNext(header);

        String[] row;
        while ((row = reader.readNext()) != null) {
            rows.add(row);
        }

        reader.close();

        Collections.shuffle(rows);

        int trainSize = (int)(rows.size() * trainRatio);

        for (int i = 0; i < rows.size(); i++) {
            if (i < trainSize) {
                trainWriter.writeNext(rows.get(i));
            } else {
                testWriter.writeNext(rows.get(i));
            }
        }

        trainWriter.close();
        testWriter.close();

        System.out.println("✅ Train/Test files created:");
        System.out.println(" - " + trainFile);
        System.out.println(" - " + testFile);
    }
}
