package com.example.ridebooking.model;

public class Driver {
    private final long id;
    private final String name;
    private final VehicleType vehicleType;
    private Location currentLocation;
    private boolean available = true;
    private int completedRides = 0;

    public Driver(long id, String name, Location currentLocation, VehicleType vehicleType) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.vehicleType = vehicleType;
    }

    public double distanceTo(Location target) {
        return currentLocation.distanceTo(target);
    }

    public void recordCompletedRide() {
        completedRides++;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public VehicleType getVehicleType() { return vehicleType; }
    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location currentLocation) { this.currentLocation = currentLocation; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public int getCompletedRides() { return completedRides; }

    @Override
    public String toString() {
        return "Driver{id=" + id + ", name='" + name + "', vehicle=" + vehicleType
                + ", location=" + currentLocation + ", available=" + available
                + ", completedRides=" + completedRides + "}";
    }
}
