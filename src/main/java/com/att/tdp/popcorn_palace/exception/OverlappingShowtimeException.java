package com.att.tdp.popcorn_palace.exception;

public class OverlappingShowtimeException extends RuntimeException {
    public OverlappingShowtimeException(String theater) {
        super("Overlapping showtime exists for theater: " + theater);
    }
} 