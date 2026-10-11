package com.example.ridebooking.model;

public class Rider {
    private final long id;
    private final String name;
    private final Location location;

    public Rider(long id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }


    public double distanceTo(Driver driver) {
        return driver.distanceTo(location);
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public Location getLocation() { return location; }

    @Override
    public String toString() {
        return "Rider{id=" + id + ", name='" + name + "', location=" + location + "}";
    }
}
