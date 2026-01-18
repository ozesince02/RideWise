package com.airtribe.ridewise.strategy;

import com.airtribe.ridewise.model.Ride;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Objects;

public final class PeakHourFareStrategy implements FareStrategy {
    private final FareStrategy baseStrategy;
    private final ZoneId zoneId;
    private final LocalTime morningStart;
    private final LocalTime morningEnd;
    private final LocalTime eveningStart;
    private final LocalTime eveningEnd;
    private final double multiplier;

    public PeakHourFareStrategy(FareStrategy baseStrategy) {
        this(baseStrategy, ZoneId.systemDefault(),
                LocalTime.of(8, 0), LocalTime.of(10, 0),
                LocalTime.of(18, 0), LocalTime.of(21, 0),
                1.5);
    }

    public PeakHourFareStrategy(
            FareStrategy baseStrategy,
            ZoneId zoneId,
            LocalTime morningStart,
            LocalTime morningEnd,
            LocalTime eveningStart,
            LocalTime eveningEnd,
            double multiplier
    ) {
        this.baseStrategy = Objects.requireNonNull(baseStrategy, "baseStrategy is required");
        this.zoneId = Objects.requireNonNull(zoneId, "zoneId is required");
        this.morningStart = Objects.requireNonNull(morningStart, "morningStart is required");
        this.morningEnd = Objects.requireNonNull(morningEnd, "morningEnd is required");
        this.eveningStart = Objects.requireNonNull(eveningStart, "eveningStart is required");
        this.eveningEnd = Objects.requireNonNull(eveningEnd, "eveningEnd is required");
        if (Double.isNaN(multiplier) || Double.isInfinite(multiplier) || multiplier < 1.0) {
            throw new IllegalArgumentException("multiplier must be a finite number >= 1.0");
        }
        this.multiplier = multiplier;
    }

    @Override
    public double calculateFare(Ride ride) {
        Objects.requireNonNull(ride, "ride is required");
        double baseFare = baseStrategy.calculateFare(ride);
        return isPeak(ride.getRequestedAt()) ? baseFare * multiplier : baseFare;
    }

    private boolean isPeak(Instant requestedAt) {
        LocalTime t = requestedAt.atZone(zoneId).toLocalTime();
        return inRange(t, morningStart, morningEnd) || inRange(t, eveningStart, eveningEnd);
    }

    private static boolean inRange(LocalTime time, LocalTime startInclusive, LocalTime endExclusive) {
        return !time.isBefore(startInclusive) && time.isBefore(endExclusive);
    }
}


