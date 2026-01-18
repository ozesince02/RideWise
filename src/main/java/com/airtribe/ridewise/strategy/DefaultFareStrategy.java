package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.VehicleType;

import java.util.Objects;

public final class DefaultFareStrategy implements FareStrategy {
    @Override
    public double calculateFare(Ride ride) {
        Objects.requireNonNull(ride, "ride is required");
        double distance = ride.getDistance();

        VehicleType vehicleType = ride.getVehicleType();
        switch (vehicleType) {
            case BIKE:
                return baseFare(10.0) + perKm(distance, 5.0);
            case AUTO:
                return baseFare(20.0) + perKm(distance, 8.0);
            case CAR:
                return baseFare(30.0) + perKm(distance, 12.0);
            default:
                // defensive; VehicleType is an enum so this shouldn't happen
                throw new IllegalStateException("Unexpected vehicle type: " + vehicleType);
        }
    }

    private static double baseFare(double base) {
        return base;
    }

    private static double perKm(double distance, double rate) {
        return distance * rate;
    }
}


