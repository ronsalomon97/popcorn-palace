package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    boolean existsByShowtimeIdAndSeatNumber(Long showtimeId, Integer seatNumber);
} 