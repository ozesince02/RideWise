package com.airtribe.ridewise;

import com.airtribe.ridewise.exception.NoDriverAvailableException;
import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.Location;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.VehicleType;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.service.RiderService;
import com.airtribe.ridewise.strategy.DefaultFareStrategy;
import com.airtribe.ridewise.strategy.FareStrategy;
import com.airtribe.ridewise.strategy.LeastActiveDriverStrategy;
import com.airtribe.ridewise.strategy.NearestDriverStrategy;
import com.airtribe.ridewise.strategy.PeakHourFareStrategy;
import com.airtribe.ridewise.strategy.RideMatchingStrategy;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public final class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.ROOT);
        Scanner scanner = new Scanner(System.in);

        RiderService riderService = new RiderService();
        DriverService driverService = new DriverService();

        RideMatchingStrategy matchingStrategy = new NearestDriverStrategy();
        FareStrategy fareStrategy = new DefaultFareStrategy();
        RideService rideService = new RideService(riderService, driverService, matchingStrategy, fareStrategy);

        while (true) {
            printMenu();
            int choice = readInt(scanner, "Choose an option: ");
            try {
                switch (choice) {
                    case 1:
                        addRider(scanner, riderService);
                        break;
                    case 2:
                        addDriver(scanner, driverService);
                        break;
                    case 3:
                        viewAvailableDrivers(driverService);
                        break;
                    case 4:
                        requestRide(scanner, riderService, rideService);
                        break;
                    case 5:
                        completeRide(scanner, rideService);
                        break;
                    case 6:
                        cancelRide(scanner, rideService);
                        break;
                    case 7:
                        viewRides(rideService);
                        break;
                    case 8:
                        System.out.println("Exiting RideWise. Bye!");
                        return;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (NoDriverAvailableException e) {
                System.out.println("No driver available: " + e.getMessage());
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("=== RideWise ===");
        System.out.println("1. Add Rider");
        System.out.println("2. Add Driver");
        System.out.println("3. View Available Drivers");
        System.out.println("4. Request Ride");
        System.out.println("5. Complete Ride");
        System.out.println("6. Cancel Ride");
        System.out.println("7. View Rides");
        System.out.println("8. Exit");
    }

    private static void addRider(Scanner scanner, RiderService riderService) {
        String name = readNonBlank(scanner, "Rider name: ");
        double x = readDouble(scanner, "Rider location X: ");
        double y = readDouble(scanner, "Rider location Y: ");
        com.airtribe.ridewise.model.Rider rider = riderService.registerRider(name, new Location(x, y));
        System.out.println("Rider registered. id=" + rider.getId());
    }

    private static void addDriver(Scanner scanner, DriverService driverService) {
        String name = readNonBlank(scanner, "Driver name: ");
        double x = readDouble(scanner, "Driver location X: ");
        double y = readDouble(scanner, "Driver location Y: ");
        Driver driver = driverService.registerDriver(name, new Location(x, y));
        System.out.println("Driver registered. id=" + driver.getId());
    }

    private static void viewAvailableDrivers(DriverService driverService) {
        List<Driver> drivers = driverService.listAvailableDrivers();
        if (drivers.isEmpty()) {
            System.out.println("No available drivers.");
            return;
        }
        System.out.println("Available drivers:");
        for (Driver d : drivers) {
            System.out.println("- " + d);
        }
    }

    private static void requestRide(Scanner scanner, RiderService riderService, RideService rideService) {
        if (riderService.listRiders().isEmpty()) {
            System.out.println("No riders registered yet. Add a rider first.");
            return;
        }

        System.out.println("Riders:");
        riderService.listRiders().forEach(r -> System.out.println("- id=" + r.getId() + ", name=" + r.getName() + ", location=" + r.getLocation()));

        String riderId = readNonBlank(scanner, "Rider id: ");

        RideMatchingStrategy matchingStrategy = chooseMatchingStrategy(scanner);
        FareStrategy fareStrategy = chooseFareStrategy(scanner);
        rideService.setRideMatchingStrategy(matchingStrategy);
        rideService.setFareStrategy(fareStrategy);

        VehicleType vehicleType = readEnum(scanner, "Vehicle type (BIKE/AUTO/CAR): ", VehicleType.class);
        double dx = readDouble(scanner, "Destination X: ");
        double dy = readDouble(scanner, "Destination Y: ");

        Ride ride = rideService.requestRide(riderId, new Location(dx, dy), vehicleType);
        System.out.println("Ride created: id=" + ride.getId() +
                ", status=" + ride.getStatus() +
                ", driverId=" + (ride.getDriver() == null ? "N/A" : ride.getDriver().getId()) +
                ", distance=" + ride.getDistance() +
                ", fare=" + (ride.getFareReceipt() == null ? "N/A" : ride.getFareReceipt().getAmount()));
    }

    private static RideMatchingStrategy chooseMatchingStrategy(Scanner scanner) {
        System.out.println("Choose matching strategy:");
        System.out.println("1. NearestDriverStrategy");
        System.out.println("2. LeastActiveDriverStrategy");
        int c = readInt(scanner, "Option: ");
        if (c == 2) {
            return new LeastActiveDriverStrategy();
        }
        return new NearestDriverStrategy();
    }

    private static FareStrategy chooseFareStrategy(Scanner scanner) {
        System.out.println("Choose fare strategy:");
        System.out.println("1. DefaultFareStrategy");
        System.out.println("2. PeakHourFareStrategy");
        int c = readInt(scanner, "Option: ");
        FareStrategy base = new DefaultFareStrategy();
        if (c == 2) {
            return new PeakHourFareStrategy(base);
        }
        return base;
    }

    private static void completeRide(Scanner scanner, RideService rideService) {
        String rideId = readNonBlank(scanner, "Ride id to complete: ");
        Ride ride = rideService.completeRide(rideId);
        System.out.println("Ride completed: " + ride);
    }

    private static void cancelRide(Scanner scanner, RideService rideService) {
        String rideId = readNonBlank(scanner, "Ride id to cancel: ");
        Ride ride = rideService.cancelRide(rideId);
        System.out.println("Ride cancelled: " + ride);
    }

    private static void viewRides(RideService rideService) {
        List<Ride> rides = rideService.listRides();
        if (rides.isEmpty()) {
            System.out.println("No rides yet.");
            return;
        }
        System.out.println("Rides:");
        for (Ride r : rides) {
            System.out.println("- " + r);
        }
    }

    private static String readNonBlank(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine();
            if (s != null && !s.trim().isEmpty()) return s.trim();
            System.out.println("Input cannot be empty. Try again.");
        }
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (Exception ignored) {
                System.out.println("Invalid integer. Try again.");
            }
        }
    }

    private static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine();
            try {
                return Double.parseDouble(s.trim());
            } catch (Exception ignored) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private static <T extends Enum<T>> T readEnum(Scanner scanner, String prompt, Class<T> enumClass) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine();
            try {
                return Enum.valueOf(enumClass, s.trim().toUpperCase(Locale.ROOT));
            } catch (Exception ignored) {
                System.out.println("Invalid value. Try again.");
            }
        }
    }
}


