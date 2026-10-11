package com.example.ridebooking.strategy;

import com.example.ridebooking.model.Ride;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "ride.fare.strategy", havingValue = "default", matchIfMissing = true)
public class DefaultFareStrategy implements FareStrategy {

    private final FareCalculator calculator;

    public DefaultFareStrategy(FareCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public double calculateFare(Ride ride) {
        return calculator.round(calculator.baseFare(ride));
    }
}
