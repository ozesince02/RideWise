package com.airtribe.ridewise.model;

import java.time.Instant;
import java.util.Objects;

public final class FareReceipt {
    private final String rideId;
    private final double amount;
    private final Instant generatedAt;

    public FareReceipt(String rideId, double amount, Instant generatedAt) {
        if (rideId == null || rideId.isBlank()) {
            throw new IllegalArgumentException("rideId is required");
        }
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount < 0.0) {
            throw new IllegalArgumentException("amount must be a finite non-negative number");
        }
        this.rideId = rideId;
        this.amount = amount;
        this.generatedAt = Objects.requireNonNull(generatedAt, "generatedAt is required");
    }

    public String getRideId() {
        return rideId;
    }

    public double getAmount() {
        return amount;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    @Override
    public String toString() {
        return "FareReceipt{" +
                "rideId='" + rideId + '\'' +
                ", amount=" + amount +
                ", generatedAt=" + generatedAt +
                '}';
    }
}


