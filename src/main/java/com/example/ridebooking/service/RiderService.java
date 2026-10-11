package com.example.ridebooking.service;

import com.example.ridebooking.exception.ResourceNotFoundException;
import com.example.ridebooking.model.Location;
import com.example.ridebooking.model.Rider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class RiderService {
    private final Map<Long, Rider> riders = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong();

    public Rider registerRider(String name, Location location) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Rider name must not be empty");
        }
        Rider rider = new Rider(idSequence.incrementAndGet(), name.trim(), location);
        riders.put(rider.getId(), rider);
        return rider;
    }

    public Rider getRiderById(long id) {
        Rider rider = riders.get(id);
        if (rider == null) {
            throw new ResourceNotFoundException("Rider", id);
        }
        return rider;
    }
}
