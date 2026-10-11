package com.example.ridebooking;

import com.example.ridebooking.exception.RideBookingException;
import com.example.ridebooking.model.Driver;
import com.example.ridebooking.model.FareReceipt;
import com.example.ridebooking.model.Location;
import com.example.ridebooking.model.Ride;
import com.example.ridebooking.model.Rider;
import com.example.ridebooking.model.VehicleType;
import com.example.ridebooking.service.DriverService;
import com.example.ridebooking.service.RideService;
import com.example.ridebooking.service.RiderService;
import java.util.List;
import java.util.Scanner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class Main implements CommandLineRunner {

    private final RiderService riderService;
    private final DriverService driverService;
    private final RideService rideService;

    public Main(RiderService riderService, DriverService driverService, RideService rideService) {
        this.riderService = riderService;
        this.driverService = driverService;
        this.rideService = rideService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = readInt(scanner, "Choose an option: ");
                running = handle(choice, scanner);
            } catch (RideBookingException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }

    private boolean handle(int choice, Scanner scanner) {
        switch (choice) {
            case 1 -> addRider(scanner);
            case 2 -> addDriver(scanner);
            case 3 -> viewAvailableDrivers();
            case 4 -> requestRide(scanner);
            case 5 -> completeRide(scanner);
            case 6 -> viewRides();
            case 7 -> { return false; }
            default -> System.out.println("Invalid option. Please choose 1-7.");
        }
        return true;
    }

    private void addRider(Scanner scanner) {
        String name = readLine(scanner, "Rider name: ");
        Location location = readLocation(scanner);
        Rider rider = riderService.registerRider(name, location);
        System.out.println("Registered: " + rider);
    }

    private void addDriver(Scanner scanner) {
        String name = readLine(scanner, "Driver name: ");
        Location location = readLocation(scanner);
        VehicleType type = readVehicleType(scanner);
        Driver driver = driverService.registerDriver(name, location, type);
        System.out.println("Registered: " + driver);
    }

    private void viewAvailableDrivers() {
        List<Driver> drivers = driverService.listAvailableDrivers();
        if (drivers.isEmpty()) {
            System.out.println("No drivers available.");
            return;
        }
        drivers.forEach(System.out::println);
    }

    private void requestRide(Scanner scanner) {
        long riderId = readLong(scanner, "Rider id: ");
        double distance = readDouble(scanner, "Trip distance (km): ");
        Ride ride = rideService.requestRide(riderId, distance);
        System.out.println("Ride assigned: " + ride);
    }

    private void completeRide(Scanner scanner) {
        long rideId = readLong(scanner, "Ride id: ");
        FareReceipt receipt = rideService.completeRide(rideId);
        System.out.printf("Ride %d completed. Fare: %.2f (generated at %s)%n",
                receipt.rideId(), receipt.amount(), receipt.generatedAt());
    }

    private void viewRides() {
        List<Ride> rides = rideService.listRides();
        if (rides.isEmpty()) {
            System.out.println("No rides yet.");
            return;
        }
        rides.forEach(System.out::println);
    }



    private void printMenu() {
        System.out.println();
        System.out.println("===== Ride Booking =====");
        System.out.println("1. Add Rider");
        System.out.println("2. Add Driver");
        System.out.println("3. View Available Drivers");
        System.out.println("4. Request Ride");
        System.out.println("5. Complete Ride");
        System.out.println("6. View Rides");
        System.out.println("7. Exit");
    }

    private Location readLocation(Scanner scanner) {
        double x = readDouble(scanner, "Location x: ");
        double y = readDouble(scanner, "Location y: ");
        return new Location(x, y);
    }

    private VehicleType readVehicleType(Scanner scanner) {
        while (true) {
            String input = readLine(scanner, "Vehicle type (BIKE/AUTO/CAR): ");
            try {
                return VehicleType.valueOf(input.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid vehicle type. Try again.");
            }
        }
    }

    private String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int readInt(Scanner scanner, String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(scanner, prompt).trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private long readLong(Scanner scanner, String prompt) {
        while (true) {
            try {
                return Long.parseLong(readLine(scanner, prompt).trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private double readDouble(Scanner scanner, String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readLine(scanner, prompt).trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
