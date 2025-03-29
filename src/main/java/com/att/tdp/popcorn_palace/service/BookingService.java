package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.request.BookingRequest;
import com.att.tdp.popcorn_palace.dto.response.BookingResponse;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request);
} 