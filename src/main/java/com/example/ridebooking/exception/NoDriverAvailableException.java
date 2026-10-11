package com.example.ridebooking.exception;

public class NoDriverAvailableException extends RideBookingException {
    public NoDriverAvailableException() {
        super("No drivers are available right now");
    }
}
