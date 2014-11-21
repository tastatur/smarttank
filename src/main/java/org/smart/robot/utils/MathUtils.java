package org.smart.robot.utils;

import java.util.Random;

public class MathUtils {
    private MathUtils() {
    }

    public static double getRandomDouble(double min, double max) {
        Random random = new Random();
        return min + (max - min) * random.nextDouble();
    }
}
