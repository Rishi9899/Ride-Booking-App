package com.example.ridebooking.strategy;

import com.example.ridebooking.model.Ride;
import org.springframework.stereotype.Component;


@Component
public class FareCalculator {
    private static final double BASE_FARE = 30.0;

    public double baseFare(Ride ride) {
        return BASE_FARE + ride.getDistance() * ride.getVehicleType().getPerKmRate();
    }

    public double round(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }
}
