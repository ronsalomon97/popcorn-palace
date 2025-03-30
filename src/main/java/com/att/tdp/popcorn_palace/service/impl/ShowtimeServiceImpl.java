package com.att.tdp.popcorn_palace.service.impl;

import com.att.tdp.popcorn_palace.dto.request.ShowtimeRequest;
import com.att.tdp.popcorn_palace.dto.response.ShowtimeResponse;
import com.att.tdp.popcorn_palace.exception.OverlappingShowtimeException;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.exception.InvalidShowtimeDurationException;
import com.att.tdp.popcorn_palace.mapper.ShowtimeMapper;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final ShowtimeMapper showtimeMapper;

    @Autowired
    public ShowtimeServiceImpl(ShowtimeRepository showtimeRepository,
                             MovieRepository movieRepository,
                             ShowtimeMapper showtimeMapper) {
        this.showtimeRepository = showtimeRepository;
        this.movieRepository = movieRepository;
        this.showtimeMapper = showtimeMapper;
    }

    @Override
    public ShowtimeResponse getShowtimeById(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", "id", id.toString()));
        return showtimeMapper.toResponse(showtime);
    }

    @Override
    @Transactional
    public ShowtimeResponse addShowtime(ShowtimeRequest request) {
        // Validate end time is after start time
        if (request.getEndTime().isBefore(request.getStartTime()) || request.getEndTime().equals(request.getStartTime())) {
            throw new InvalidShowtimeDurationException();
        }

        Movie movie = movieRepository.findByTitle(request.getMovieTitle())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "title", request.getMovieTitle()));

        // Check for overlapping showtimes
        List<Showtime> overlappingShowtimes = showtimeRepository.findOverlappingShowtimes(
                request.getTheater(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (!overlappingShowtimes.isEmpty()) {
            throw new OverlappingShowtimeException(request.getTheater());
        }

        Showtime showtime = showtimeMapper.toEntity(request, movie);
        
        Showtime savedShowtime = showtimeRepository.save(showtime);
        return showtimeMapper.toResponse(savedShowtime);
    }

    @Override
    @Transactional
    public void updateShowtime(Long id, ShowtimeRequest request) {
        // Validate end time is after start time
        if (request.getEndTime().isBefore(request.getStartTime()) || request.getEndTime().equals(request.getStartTime())) {
            throw new InvalidShowtimeDurationException();
        }

        Showtime existingShowtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", "id", id.toString()));

        Movie movie = movieRepository.findByTitle(request.getMovieTitle())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "title", request.getMovieTitle()));

        // Check for overlapping showtimes (excluding current showtime)
        List<Showtime> overlappingShowtimes = showtimeRepository.findOverlappingShowtimes(
                request.getTheater(),
                request.getStartTime(),
                request.getEndTime()
        ).stream()
                .filter(s -> !s.getId().equals(id))
                .toList();

        if (!overlappingShowtimes.isEmpty()) {
            throw new OverlappingShowtimeException(request.getTheater());
        }

        showtimeMapper.updateEntity(existingShowtime, request, movie);
        showtimeRepository.save(existingShowtime);
    }

    @Override
    @Transactional
    public void deleteShowtime(Long id) {
        if (!showtimeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Showtime", "id", id.toString());
        }
        showtimeRepository.deleteById(id);
    }
}