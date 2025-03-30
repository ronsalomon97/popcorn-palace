package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    @Query("SELECT s FROM Showtime s WHERE s.theater = :theater " +
           "AND ((s.startTime <= :endTime AND s.endTime >= :startTime) " +
           "OR (s.startTime >= :startTime AND s.startTime <= :endTime))")
    List<Showtime> findOverlappingShowtimes(String theater, 
                                          LocalDateTime startTime, 
                                          LocalDateTime endTime);

    /**
     * Check if there are any showtimes for a specific movie
     * 
     * @param movieId the ID of the movie
     * @return true if there are showtimes for the movie, false otherwise
     */
    boolean existsByMovieId(Long movieId);
} 