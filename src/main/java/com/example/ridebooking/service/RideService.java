package com.example.ridebooking.service;

import com.example.ridebooking.exception.InvalidRideStateException;
import com.example.ridebooking.exception.NoDriverAvailableException;
import com.example.ridebooking.exception.ResourceNotFoundException;
import com.example.ridebooking.model.Driver;
import com.example.ridebooking.model.FareReceipt;
import com.example.ridebooking.model.Ride;
import com.example.ridebooking.model.RideStatus;
import com.example.ridebooking.model.Rider;
import com.example.ridebooking.strategy.FareStrategy;
import com.example.ridebooking.strategy.RideMatchingStrategy;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class RideService {
    private final RiderService riderService;
    private final DriverService driverService;
    private final RideMatchingStrategy matchingStrategy;
    private final FareStrategy fareStrategy;
    private final Clock clock;

    private final Map<Long, Ride> rides = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong();

    // Strategies are injected as interfaces -> DIP + OCP
    public RideService(RiderService riderService,
                       DriverService driverService,
                       RideMatchingStrategy matchingStrategy,
                       FareStrategy fareStrategy,
                       Clock clock) {
        this.riderService = riderService;
        this.driverService = driverService;
        this.matchingStrategy = matchingStrategy;
        this.fareStrategy = fareStrategy;
        this.clock = clock;
    }

    public Ride requestRide(long riderId, double distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("Distance must be greater than 0");
        }
        Rider rider = riderService.getRiderById(riderId);
        Ride ride = new Ride(idSequence.incrementAndGet(), rider, distance);
        rides.put(ride.getId(), ride);

        List<Driver> available = driverService.listAvailableDrivers();
        if (available.isEmpty()) {
            ride.cancel();
            throw new NoDriverAvailableException();
        }
        assignDriver(ride, matchingStrategy.findDriver(rider, available));
        return ride;
    }

    public FareReceipt completeRide(long rideId) {
        Ride ride = getRideById(rideId);
        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new InvalidRideStateException(
                    "Only ASSIGNED rides can be completed (ride " + rideId + " is " + ride.getStatus() + ")");
        }
        double amount = fareStrategy.calculateFare(ride);
        ride.complete();
        releaseDriver(ride);
        return new FareReceipt(ride.getId(), amount, LocalDateTime.now(clock));
    }

    public void cancelRide(long rideId) {
        Ride ride = getRideById(rideId);
        if (ride.getStatus() == RideStatus.COMPLETED || ride.getStatus() == RideStatus.CANCELLED) {
            throw new InvalidRideStateException("Ride " + rideId + " is already " + ride.getStatus());
        }
        boolean wasAssigned = ride.getStatus() == RideStatus.ASSIGNED;
        ride.cancel();
        if (wasAssigned) {
            driverService.updateAvailability(ride.getDriver().getId(), true);
        }
    }

    public Ride getRideById(long id) {
        Ride ride = rides.get(id);
        if (ride == null) {
            throw new ResourceNotFoundException("Ride", id);
        }
        return ride;
    }

    public List<Ride> listRides() {
        return rides.values().stream()
                .sorted(Comparator.comparingLong(Ride::getId))
                .toList();
    }

    private void assignDriver(Ride ride, Driver driver) {
        driverService.updateAvailability(driver.getId(), false);
        ride.assign(driver);
    }

    private void releaseDriver(Ride ride) {
        Driver driver = ride.getDriver();
        driver.recordCompletedRide();
        driverService.updateAvailability(driver.getId(), true);
    }
}
