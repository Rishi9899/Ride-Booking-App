package com.example.ridebooking.strategy;

import com.example.ridebooking.exception.NoDriverAvailableException;
import com.example.ridebooking.model.Driver;
import com.example.ridebooking.model.Rider;
import java.util.Comparator;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "ride.matching.strategy", havingValue = "nearest", matchIfMissing = true)
public class NearestDriverStrategy implements RideMatchingStrategy {

    @Override
    public Driver findDriver(Rider rider, List<Driver> drivers) {
        return drivers.stream()
                .min(Comparator.comparingDouble(rider::distanceTo))
                .orElseThrow(NoDriverAvailableException::new);
    }
}
