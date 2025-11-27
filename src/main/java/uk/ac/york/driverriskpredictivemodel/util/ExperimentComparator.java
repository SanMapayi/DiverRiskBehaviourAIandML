package uk.ac.york.driverriskpredictivemodel.util;

import java.io.*;
import java.util.Objects;

public class ExperimentComparator {

    public static void main(String[] args) throws Exception {

        String time = java.time.LocalDateTime.now()
                .toString()
                .replace(":", "-");

        File base = new File("docs/Experiments");
        File comparisonBase = new File(base, "comparison");
        File latest = new File(comparisonBase, "latest");
        File history = new File(comparisonBase, "history");

        latest.mkdirs();
        history.mkdirs();

        File latestFile = new File(latest, "experiment-comparison.csv");
        File historyFile = new File(history, "experiment-comparison_" + time + ".csv");

        FileWriter fwLatest = new FileWriter(latestFile);
        FileWriter fwHistory = new FileWriter(historyFile);

        fwLatest.write("Run,Safe_Correct,Risky_Correct\n");
        fwHistory.write("Run,Safe_Correct,Risky_Correct\n");

        for (File run : Objects.requireNonNull(base.listFiles())) {
            if (!run.isDirectory() || !run.getName().startsWith("run_")) continue;

            File cmFile = new File(run, "confusion-matrix.csv");
            if (!cmFile.exists()) continue;

            BufferedReader br = new BufferedReader(new FileReader(cmFile));

            String line;
            int safeCorrect = -1;
            int riskyCorrect = -1;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("Actual_0")) {
                    String[] parts = line.split(",");
                    safeCorrect = Integer.parseInt(parts[1]);
                }
                if (line.startsWith("Actual_1")) {
                    String[] parts = line.split(",");
                    riskyCorrect = Integer.parseInt(parts[2]);
                }
            }
            br.close();

            String row = run.getName() + "," + safeCorrect + "," + riskyCorrect + "\n";

            fwLatest.write(row);
            fwHistory.write(row);
        }

        fwLatest.close();
        fwHistory.close();

        System.out.println("✅ Comparison CSV generated:");
        System.out.println(latestFile.getAbsolutePath());
        System.out.println(historyFile.getAbsolutePath());
    }
}
