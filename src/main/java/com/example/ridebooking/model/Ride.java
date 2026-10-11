package com.example.ridebooking.model;

public class Ride {
    private final long id;
    private final Rider rider;
    private final double distance;
    private Driver driver;
    private RideStatus status = RideStatus.REQUESTED;

    public Ride(long id, Rider rider, double distance) {
        this.id = id;
        this.rider = rider;
        this.distance = distance;
    }

    public VehicleType getVehicleType() {
        return driver.getVehicleType();
    }

    public void assign(Driver driver) {
        this.driver = driver;
        this.status = RideStatus.ASSIGNED;
    }

    public void complete() { this.status = RideStatus.COMPLETED; }
    public void cancel() { this.status = RideStatus.CANCELLED; }

    public long getId() { return id; }
    public Rider getRider() { return rider; }
    public Driver getDriver() { return driver; }
    public double getDistance() { return distance; }
    public RideStatus getStatus() { return status; }

    @Override
    public String toString() {
        return "Ride{id=" + id + ", rider=" + rider.getName()
                + ", driver=" + (driver == null ? "-" : driver.getName())
                + ", distance=" + distance + "km, status=" + status + "}";
    }
}
