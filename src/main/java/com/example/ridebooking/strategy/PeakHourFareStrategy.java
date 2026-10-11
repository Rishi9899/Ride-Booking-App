package com.example.ridebooking.strategy;

import com.example.ridebooking.model.Ride;
import java.time.Clock;
import java.time.LocalTime;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Component
@ConditionalOnProperty(name = "ride.fare.strategy", havingValue = "peak")
public class PeakHourFareStrategy implements FareStrategy {
    private static final double SURGE = 1.5;

    private final FareCalculator calculator;
    private final Clock clock;

    public PeakHourFareStrategy(FareCalculator calculator, Clock clock) {
        this.calculator = calculator;
        this.clock = clock;
    }

    @Override
    public double calculateFare(Ride ride) {
        double fare = calculator.baseFare(ride);
        return calculator.round(isPeak() ? fare * SURGE : fare);
    }

    private boolean isPeak() {
        int hour = LocalTime.now(clock).getHour();
        return (hour >= 8 && hour < 10) || (hour >= 17 && hour < 20);
    }
}
