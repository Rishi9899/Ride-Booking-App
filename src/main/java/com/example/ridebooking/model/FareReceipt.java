package com.example.ridebooking.model;

import java.time.LocalDateTime;

public record FareReceipt(long rideId, double amount, LocalDateTime generatedAt) {
}
