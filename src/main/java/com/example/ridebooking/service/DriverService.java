package com.example.ridebooking.service;

import com.example.ridebooking.exception.ResourceNotFoundException;
import com.example.ridebooking.model.Driver;
import com.example.ridebooking.model.Location;
import com.example.ridebooking.model.VehicleType;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class DriverService {
    private final Map<Long, Driver> drivers = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong();

    public Driver registerDriver(String name, Location location, VehicleType vehicleType) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Driver name must not be empty");
        }
        Driver driver = new Driver(idSequence.incrementAndGet(), name.trim(), location, vehicleType);
        drivers.put(driver.getId(), driver);
        return driver;
    }

    public Driver getDriverById(long id) {
        Driver driver = drivers.get(id);
        if (driver == null) {
            throw new ResourceNotFoundException("Driver", id);
        }
        return driver;
    }

    public void updateAvailability(long driverId, boolean available) {
        getDriverById(driverId).setAvailable(available);
    }

    public List<Driver> listAvailableDrivers() {
        return drivers.values().stream()
                .filter(Driver::isAvailable)
                .sorted(Comparator.comparingLong(Driver::getId))
                .toList();
    }
}
