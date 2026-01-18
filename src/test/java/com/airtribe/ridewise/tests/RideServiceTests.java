package com.airtribe.ridewise.tests;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.RideStatus;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.service.RiderService;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;

import static com.airtribe.ridewise.tests.Assertions.*;

public final class RideServiceTests {
    private RideServiceTests() {}

    public static void runAll() {
        requestRide_assignsDriverAndCreatesFareReceipt();
        completeRide_marksDriverAvailableAndRideCompleted();
        cancelRide_marksDriverAvailableAndRideCancelled();
        requestRide_throwsWhenNoDriversAvailable();
    }

    private static void requestRide_assignsDriverAndCreatesFareReceipt() {
        RiderService riderService = new RiderService();
        DriverService driverService = new DriverService();

        var rider = riderService.registerRider("R", new Location(0, 0));
        var driver = driverService.registerDriver("D", new Location(1, 1));

        RideService rideService = new RideService(riderService, driverService, new NearestDriverStrategy(), new DefaultFareStrategy());
        var ride = rideService.requestRide(rider.getId(), new Location(3, 4), VehicleType.CAR);

        assertEquals(RideStatus.ASSIGNED, ride.getStatus(), "Expected ASSIGNED ride");
        assertNotNull(ride.getDriver(), "Expected assigned driver");
        assertEquals(driver.getId(), ride.getDriver().getId(), "Expected chosen driver");
        assertNotNull(ride.getFareReceipt(), "Expected fare receipt");
        assertFalse(driver.isAvailable(), "Expected driver to be unavailable after assignment");
    }

    private static void completeRide_marksDriverAvailableAndRideCompleted() {
        RiderService riderService = new RiderService();
        DriverService driverService = new DriverService();

        var rider = riderService.registerRider("R", new Location(0, 0));
        var driver = driverService.registerDriver("D", new Location(1, 1));

        RideService rideService = new RideService(riderService, driverService, new NearestDriverStrategy(), new DefaultFareStrategy());
        var ride = rideService.requestRide(rider.getId(), new Location(1, 0), VehicleType.BIKE);

        var completed = rideService.completeRide(ride.getId());
        assertEquals(RideStatus.COMPLETED, completed.getStatus(), "Expected COMPLETED ride");
        assertTrue(driver.isAvailable(), "Expected driver available after completion");
        assertEquals(1, driver.getRidesCompleted(), "Expected driver ride count incremented");
    }

    private static void cancelRide_marksDriverAvailableAndRideCancelled() {
        RiderService riderService = new RiderService();
        DriverService driverService = new DriverService();

        var rider = riderService.registerRider("R", new Location(0, 0));
        var driver = driverService.registerDriver("D", new Location(1, 1));

        RideService rideService = new RideService(riderService, driverService, new NearestDriverStrategy(), new DefaultFareStrategy());
        var ride = rideService.requestRide(rider.getId(), new Location(1, 0), VehicleType.AUTO);

        var cancelled = rideService.cancelRide(ride.getId());
        assertEquals(RideStatus.CANCELLED, cancelled.getStatus(), "Expected CANCELLED ride");
        assertTrue(driver.isAvailable(), "Expected driver available after cancellation");
    }

    private static void requestRide_throwsWhenNoDriversAvailable() {
        RiderService riderService = new RiderService();
        DriverService driverService = new DriverService();

        var rider = riderService.registerRider("R", new Location(0, 0));
        RideService rideService = new RideService(riderService, driverService, new NearestDriverStrategy(), new DefaultFareStrategy());

        assertThrows(NoDriverAvailableException.class,
                () -> rideService.requestRide(rider.getId(), new Location(1, 1), VehicleType.AUTO),
                "Expected NoDriverAvailableException");
    }
}


