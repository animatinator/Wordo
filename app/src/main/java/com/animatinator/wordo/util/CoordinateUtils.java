package com.animatinator.wordo.util;

public class CoordinateUtils {
    public static double distance(Coordinates first, Coordinates second) {
        float x = second.x() - first.x();
        float y = second.y() - first.y();
        return Math.sqrt(x * x + y * y);
    }
}