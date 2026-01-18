package com.airtribe.ridewise.service;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.util.IdGenerator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class DriverService {
    private final Map<String, Driver> driversById = new LinkedHashMap<>();

    public Driver registerDriver(String name, Location currentLocation) {
        String id = IdGenerator.newId();
        Driver driver = new Driver(id, name, currentLocation, true);
        driversById.put(id, driver);
        return driver;
    }

    public Optional<Driver> findById(String driverId) {
        if (driverId == null || driverId.isBlank()) return Optional.empty();
        return Optional.ofNullable(driversById.get(driverId));
    }

    public Driver getByIdOrThrow(String driverId) {
        Objects.requireNonNull(driverId, "driverId is required");
        Driver driver = driversById.get(driverId);
        if (driver == null) {
            throw new IllegalArgumentException("Driver not found: " + driverId);
        }
        return driver;
    }

    public void updateAvailability(String driverId, boolean available) {
        Driver driver = getByIdOrThrow(driverId);
        driver.setAvailable(available);
    }

    public List<Driver> listAllDrivers() {
        return new ArrayList<>(driversById.values());
    }

    public List<Driver> listAvailableDrivers() {
        return driversById.values().stream().filter(Driver::isAvailable).toList();
    }
}


