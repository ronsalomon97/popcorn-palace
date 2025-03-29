package com.att.tdp.popcorn_palace.exception;

public class SeatAlreadyBookedException extends RuntimeException {
    public SeatAlreadyBookedException(Integer seatNumber, Long showtimeId) {
        super(String.format("Seat %d is already booked for showtime %d", seatNumber, showtimeId));
    }
} 