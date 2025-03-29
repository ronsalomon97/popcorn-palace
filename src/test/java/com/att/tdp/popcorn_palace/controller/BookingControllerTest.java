package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.request.BookingRequest;
import com.att.tdp.popcorn_palace.dto.response.BookingResponse;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.exception.SeatAlreadyBookedException;
import com.att.tdp.popcorn_palace.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBooking_Success() {
        // Arrange
        BookingRequest request = createBookingRequest(1L, 15, UUID.randomUUID());
        BookingResponse expectedResponse = createBookingResponse(UUID.randomUUID());
        when(bookingService.createBooking(any(BookingRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<BookingResponse> response = bookingController.createBooking(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(bookingService).createBooking(request);
    }

    @Test
    void createBooking_ShowtimeNotFound() {
        // Arrange
        BookingRequest request = createBookingRequest(1L, 15, UUID.randomUUID());
        when(bookingService.createBooking(any(BookingRequest.class)))
            .thenThrow(new ResourceNotFoundException("Showtime", "id", "1"));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookingController.createBooking(request));
        verify(bookingService).createBooking(request);
    }

    @Test
    void createBooking_SeatAlreadyBooked() {
        // Arrange
        BookingRequest request = createBookingRequest(1L, 15, UUID.randomUUID());
        when(bookingService.createBooking(any(BookingRequest.class)))
            .thenThrow(new SeatAlreadyBookedException(15, 1L));

        // Act & Assert
        assertThrows(SeatAlreadyBookedException.class, () -> bookingController.createBooking(request));
        verify(bookingService).createBooking(request);
    }

    private BookingRequest createBookingRequest(Long showtimeId, Integer seatNumber, UUID userId) {
        BookingRequest request = new BookingRequest();
        request.setShowtimeId(showtimeId);
        request.setSeatNumber(seatNumber);
        request.setUserId(userId);
        return request;
    }

    private BookingResponse createBookingResponse(UUID bookingId) {
        BookingResponse response = new BookingResponse(bookingId);
        response.setBookingId(bookingId);
        return response;
    }
} 