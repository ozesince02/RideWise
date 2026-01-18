package com.airtribe.ridewise.service;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;
import com.airtribe.ridewise.util.DistanceUtil;
import com.airtribe.ridewise.util.IdGenerator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class RideService {
    private final RiderService riderService;
    private final DriverService driverService;

    private RideMatchingStrategy rideMatchingStrategy;
    private FareStrategy fareStrategy;

    private final Map<String, Ride> ridesById = new LinkedHashMap<>();

    public RideService(
            RiderService riderService,
            DriverService driverService,
            RideMatchingStrategy rideMatchingStrategy,
            FareStrategy fareStrategy
    ) {
        this.riderService = Objects.requireNonNull(riderService, "riderService is required");
        this.driverService = Objects.requireNonNull(driverService, "driverService is required");
        this.rideMatchingStrategy = Objects.requireNonNull(rideMatchingStrategy, "rideMatchingStrategy is required");
        this.fareStrategy = Objects.requireNonNull(fareStrategy, "fareStrategy is required");
    }

    public void setRideMatchingStrategy(RideMatchingStrategy rideMatchingStrategy) {
        this.rideMatchingStrategy = Objects.requireNonNull(rideMatchingStrategy, "rideMatchingStrategy is required");
    }

    public void setFareStrategy(FareStrategy fareStrategy) {
        this.fareStrategy = Objects.requireNonNull(fareStrategy, "fareStrategy is required");
    }

    public Ride requestRide(String riderId, Location destination, VehicleType vehicleType) {
        Objects.requireNonNull(destination, "destination is required");
        Objects.requireNonNull(vehicleType, "vehicleType is required");

        Rider rider = riderService.getByIdOrThrow(riderId);
        double distance = DistanceUtil.euclideanDistance(rider.getLocation(), destination);

        Ride ride = new Ride(
                IdGenerator.newId(),
                rider,
                distance,
                RideStatus.REQUESTED,
                vehicleType,
                Instant.now()
        );

        Driver driver = rideMatchingStrategy.findDriver(rider, driverService.listAllDrivers());
        if (driver == null) {
            throw new NoDriverAvailableException("No available driver found for rider: " + riderId);
        }

        driver.setAvailable(false);
        ride.assignDriver(driver);
        ride.setStatus(RideStatus.ASSIGNED);

        double amount = fareStrategy.calculateFare(ride);
        ride.setFareReceipt(new FareReceipt(ride.getId(), amount, Instant.now()));

        ridesById.put(ride.getId(), ride);
        return ride;
    }

    public Ride completeRide(String rideId) {
        Ride ride = getByIdOrThrow(rideId);
        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new IllegalStateException("Only ASSIGNED rides can be completed. rideId=" + rideId);
        }
        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(Instant.now());

        Driver driver = ride.getDriver();
        if (driver != null) {
            driver.setAvailable(true);
            driver.incrementRidesCompleted();
        }
        return ride;
    }

    public Ride cancelRide(String rideId) {
        Ride ride = getByIdOrThrow(rideId);
        if (ride.getStatus() == RideStatus.COMPLETED) {
            throw new IllegalStateException("Completed rides cannot be cancelled. rideId=" + rideId);
        }
        if (ride.getStatus() == RideStatus.CANCELLED) {
            return ride;
        }

        ride.setStatus(RideStatus.CANCELLED);
        Driver driver = ride.getDriver();
        if (driver != null) {
            driver.setAvailable(true);
        }
        return ride;
    }

    public Ride getByIdOrThrow(String rideId) {
        Objects.requireNonNull(rideId, "rideId is required");
        Ride ride = ridesById.get(rideId);
        if (ride == null) {
            throw new IllegalArgumentException("Ride not found: " + rideId);
        }
        return ride;
    }

    public List<Ride> listRides() {
        return new ArrayList<>(ridesById.values());
    }
}


