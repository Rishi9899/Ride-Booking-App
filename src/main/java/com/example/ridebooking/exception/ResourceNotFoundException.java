package com.example.ridebooking.exception;

public class ResourceNotFoundException extends RideBookingException {
    public ResourceNotFoundException(String type, long id) {
        super(type + " not found with id " + id);
    }
}
