package com.att.tdp.popcorn_palace.exception;

public class InvalidShowtimeDurationException extends RuntimeException {
    public InvalidShowtimeDurationException() {
        super("End time must be after start time");
    }
} 