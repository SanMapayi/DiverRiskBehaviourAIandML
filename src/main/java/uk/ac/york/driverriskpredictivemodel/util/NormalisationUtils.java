package uk.ac.york.driverriskpredictivemodel.util;
public class NormalisationUtils {

    public static double minMax(double value, double min, double max) {
        return (value - min) / (max - min + 1e-9);
    }
}
