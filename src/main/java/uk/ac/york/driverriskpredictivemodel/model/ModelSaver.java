package uk.ac.york.driverriskpredictivemodel.model;


import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModelSaver {

    public void save(MultiLayerNetwork model, String path) {
        try {
            Path p = Path.of(path);

            // Ensure folder exists
            Files.createDirectories(p.getParent());

            model.save(p.toFile());

            System.out.println("✅ Model saved to: " + p.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("❌ Failed to save model:");
            e.printStackTrace();
        }
    }

    public MultiLayerNetwork load(String path) throws IOException {
        return MultiLayerNetwork.load(new File(path), true);
    }
}
