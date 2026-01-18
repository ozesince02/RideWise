package com.airtribe.ridewise.model;

import java.util.Objects;

public final class Driver {
    private final String id;
    private final String name;
    private Location currentLocation;
    private boolean available;
    private int ridesCompleted;

    public Driver(String id, String name, Location currentLocation, boolean available) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        this.id = id;
        this.name = name;
        this.currentLocation = Objects.requireNonNull(currentLocation, "currentLocation is required");
        this.available = available;
        this.ridesCompleted = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = Objects.requireNonNull(currentLocation, "currentLocation is required");
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getRidesCompleted() {
        return ridesCompleted;
    }

    public void incrementRidesCompleted() {
        this.ridesCompleted++;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", currentLocation=" + currentLocation +
                ", available=" + available +
                ", ridesCompleted=" + ridesCompleted +
                '}';
    }
}


