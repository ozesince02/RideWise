package com.airtribe.ridewise.tests;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.model.Rider;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.LeastActiveDriverStrategy;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;
import com.airtribe.ridewise.strategy.PeakHourFareStrategy;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.airtribe.ridewise.tests.Assertions.*;

public final class StrategyTests {
    private StrategyTests() {}

    public static void runAll() {
        nearestDriverStrategy_picksNearestAvailable();
        nearestDriverStrategy_ignoresUnavailable();
        leastActiveDriverStrategy_picksLeastActive();
        leastActiveDriverStrategy_tiesBreakByDistanceThenId();
        defaultFareStrategy_usesVehicleRates();
        peakHourFareStrategy_appliesMultiplierDuringWindow();
    }

    private static void nearestDriverStrategy_picksNearestAvailable() {
        Rider rider = new Rider("r1", "R", new Location(0, 0));
        Driver far = new Driver("d2", "Far", new Location(10, 10), true);
        Driver near = new Driver("d1", "Near", new Location(1, 1), true);

        var strategy = new NearestDriverStrategy();
        var chosen = strategy.findDriver(rider, List.of(far, near));

        assertNotNull(chosen, "Expected a driver");
        assertEquals("d1", chosen.getId(), "Expected nearest driver");
    }

    private static void nearestDriverStrategy_ignoresUnavailable() {
        Rider rider = new Rider("r1", "R", new Location(0, 0));
        Driver unavailableNear = new Driver("d1", "Near", new Location(1, 1), false);
        Driver availableFar = new Driver("d2", "Far", new Location(10, 10), true);

        var strategy = new NearestDriverStrategy();
        var chosen = strategy.findDriver(rider, List.of(unavailableNear, availableFar));

        assertNotNull(chosen, "Expected a driver");
        assertEquals("d2", chosen.getId(), "Expected available driver");
    }

    private static void leastActiveDriverStrategy_picksLeastActive() {
        Rider rider = new Rider("r1", "R", new Location(0, 0));

        Driver d1 = new Driver("d1", "D1", new Location(5, 5), true);
        d1.incrementRidesCompleted(); // 1
        Driver d2 = new Driver("d2", "D2", new Location(10, 10), true); // 0

        var strategy = new LeastActiveDriverStrategy();
        var chosen = strategy.findDriver(rider, List.of(d1, d2));

        assertNotNull(chosen, "Expected a driver");
        assertEquals("d2", chosen.getId(), "Expected least active driver");
    }

    private static void leastActiveDriverStrategy_tiesBreakByDistanceThenId() {
        Rider rider = new Rider("r1", "R", new Location(0, 0));

        Driver a = new Driver("a", "A", new Location(2, 0), true);
        Driver b = new Driver("b", "B", new Location(2, 0), true);

        var strategy = new LeastActiveDriverStrategy();
        var chosen = strategy.findDriver(rider, List.of(b, a));

        assertNotNull(chosen, "Expected a driver");
        assertEquals("a", chosen.getId(), "Expected deterministic tie-break by id");
    }

    private static void defaultFareStrategy_usesVehicleRates() {
        Rider rider = new Rider("r1", "R", new Location(0, 0));
        Ride ride = new Ride("ride1", rider, 2.0, RideStatus.REQUESTED, VehicleType.BIKE, Instant.now());
        double fare = new DefaultFareStrategy().calculateFare(ride);
        assertEqualsDouble(10.0 + (2.0 * 5.0), fare, 0.0001, "Expected BIKE fare computation");
    }

    private static void peakHourFareStrategy_appliesMultiplierDuringWindow() {
        Rider rider = new Rider("r1", "R", new Location(0, 0));
        ZoneId zone = ZoneId.systemDefault();
        Instant peakInstant = ZonedDateTime.now(zone).withHour(8).withMinute(30).withSecond(0).withNano(0).toInstant();
        Ride ride = new Ride("ride1", rider, 1.0, RideStatus.REQUESTED, VehicleType.AUTO, peakInstant);

        var base = new DefaultFareStrategy();
        var peak = new PeakHourFareStrategy(base);
        double baseFare = base.calculateFare(ride);
        double peakFare = peak.calculateFare(ride);

        assertEqualsDouble(baseFare * 1.5, peakFare, 0.0001, "Expected peak multiplier");
    }
}


