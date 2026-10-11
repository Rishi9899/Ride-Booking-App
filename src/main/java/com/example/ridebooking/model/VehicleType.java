package com.example.ridebooking.model;

public enum VehicleType {
    BIKE(8.0), AUTO(12.0), CAR(18.0);

    private final double perKmRate;

    VehicleType(double perKmRate) {
        this.perKmRate = perKmRate;
    }

    public double getPerKmRate() {
        return perKmRate;
    }
}
