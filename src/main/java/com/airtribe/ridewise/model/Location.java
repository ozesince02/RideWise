package com.airtribe.ridewise.model;

import java.util.Objects;

/**
 * Value object representing a 2D coordinate.
 */
public final class Location {
    private final double x;
    private final double y;

    public Location(double x, double y) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("x must be a finite number");
        }
        if (Double.isNaN(y) || Double.isInfinite(y)) {
            throw new IllegalArgumentException("y must be a finite number");
        }
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Double.compare(x, location.x) == 0 && Double.compare(y, location.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Location{" + "x=" + x + ", y=" + y + '}';
    }
}


