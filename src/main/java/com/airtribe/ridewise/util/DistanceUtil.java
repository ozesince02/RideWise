package com.airtribe.ridewise.util;

import com.airtribe.ridewise.model.Location;

public final class DistanceUtil {
    private DistanceUtil() {
        // utility
    }

    public static double euclideanDistance(Location a, Location b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Locations must not be null");
        }
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}


