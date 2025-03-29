package com.att.tdp.popcorn_palace.service.impl;

import com.att.tdp.popcorn_palace.dto.request.BookingRequest;
import com.att.tdp.popcorn_palace.dto.response.BookingResponse;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.exception.SeatAlreadyBookedException;
import com.att.tdp.popcorn_palace.model.Booking;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, 
                            ShowtimeRepository showtimeRepository) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
    }

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // Check if showtime exists
        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", "id", 
                        request.getShowtimeId().toString()));

        // Check if seat is already booked
        if (bookingRepository.existsByShowtimeIdAndSeatNumber(
                request.getShowtimeId(), request.getSeatNumber())) {
            throw new SeatAlreadyBookedException(request.getSeatNumber(), request.getShowtimeId());
        }

        // Create new booking
        Booking booking = new Booking();
        booking.setShowtime(showtime);
        booking.setSeatNumber(request.getSeatNumber());
        booking.setUserId(request.getUserId());

        // Save booking
        Booking savedBooking = bookingRepository.save(booking);
        return new BookingResponse(savedBooking.getId());
    }
} 