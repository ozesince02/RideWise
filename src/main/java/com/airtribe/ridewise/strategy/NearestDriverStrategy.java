package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.util.DistanceUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class NearestDriverStrategy implements RideMatchingStrategy {
    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        Objects.requireNonNull(rider, "rider is required");
        Objects.requireNonNull(drivers, "drivers is required");

        return drivers.stream()
                .filter(Objects::nonNull)
                .filter(Driver::isAvailable)
                .min(Comparator
                        .comparingDouble((Driver d) -> DistanceUtil.euclideanDistance(rider.getLocation(), d.getCurrentLocation()))
                        .thenComparing(Driver::getId))
                .orElse(null);
    }
}


