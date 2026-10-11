package com.example.ridebooking.strategy;

import com.example.ridebooking.exception.NoDriverAvailableException;
import com.example.ridebooking.model.Driver;
import com.example.ridebooking.model.Rider;
import java.util.Comparator;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Component
@ConditionalOnProperty(name = "ride.matching.strategy", havingValue = "least-active")
public class LeastActiveDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        return drivers.stream()
                .min(Comparator.comparingInt(Driver::getCompletedRides)
                        .thenComparingDouble(rider::distanceTo))
                .orElseThrow(NoDriverAvailableException::new);
    }
}
