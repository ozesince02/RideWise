package com.airtribe.ridewise.model;

import java.util.Objects;

public final class Rider {
    private final String id;
    private final String name;
    private Location location;

    public Rider(String id, String name, Location location) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        this.id = id;
        this.name = name;
        this.location = Objects.requireNonNull(location, "location is required");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = Objects.requireNonNull(location, "location is required");
    }

    @Override
    public String toString() {
        return "Rider{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location=" + location +
                '}';
    }
}


