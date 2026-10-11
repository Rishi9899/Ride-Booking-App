package com.example.ridebooking.strategy;

import com.example.ridebooking.model.Ride;

public interface FareStrategy {
    double calculateFare(Ride ride);
}
