package com.airtribe.ridewise.model;

import java.time.Instant;
import java.util.Objects;

public final class Ride {
    private final String id;
    private final Rider rider;
    private Driver driver; // assigned later
    private final double distance;
    private RideStatus status;
    private final VehicleType vehicleType;
    private final Instant requestedAt;
    private Instant completedAt;
    private FareReceipt fareReceipt; // composed by RideService when fare is calculated

    public Ride(
            String id,
            Rider rider,
            double distance,
            RideStatus status,
            VehicleType vehicleType,
            Instant requestedAt
    ) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }
        this.id = id;
        this.rider = Objects.requireNonNull(rider, "rider is required");
        if (Double.isNaN(distance) || Double.isInfinite(distance) || distance < 0.0) {
            throw new IllegalArgumentException("distance must be a finite non-negative number");
        }
        this.distance = distance;
        this.status = Objects.requireNonNull(status, "status is required");
        this.vehicleType = Objects.requireNonNull(vehicleType, "vehicleType is required");
        this.requestedAt = Objects.requireNonNull(requestedAt, "requestedAt is required");
    }

    public String getId() {
        return id;
    }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public void assignDriver(Driver driver) {
        this.driver = Objects.requireNonNull(driver, "driver is required");
    }

    public double getDistance() {
        return distance;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = Objects.requireNonNull(status, "status is required");
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public Instant getRequestedAt() {
        return requestedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = Objects.requireNonNull(completedAt, "completedAt is required");
    }

    public FareReceipt getFareReceipt() {
        return fareReceipt;
    }

    public void setFareReceipt(FareReceipt fareReceipt) {
        this.fareReceipt = Objects.requireNonNull(fareReceipt, "fareReceipt is required");
    }

    @Override
    public String toString() {
        return "Ride{" +
                "id='" + id + '\'' +
                ", rider=" + rider +
                ", driver=" + driver +
                ", distance=" + distance +
                ", status=" + status +
                ", vehicleType=" + vehicleType +
                ", requestedAt=" + requestedAt +
                ", completedAt=" + completedAt +
                ", fareReceipt=" + fareReceipt +
                '}';
    }
}


