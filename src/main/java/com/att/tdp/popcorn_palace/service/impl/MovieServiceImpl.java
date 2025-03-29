package com.att.tdp.popcorn_palace.service.impl;

import com.att.tdp.popcorn_palace.dto.request.MovieRequest;
import com.att.tdp.popcorn_palace.dto.response.MovieResponse;
import com.att.tdp.popcorn_palace.exception.DuplicateMovieTitleException;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.mapper.MovieMapper;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    @Override
    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(movieMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MovieResponse addMovie(MovieRequest request) {
        if (movieRepository.findByTitle(request.getTitle()).isPresent()) {
            throw new DuplicateMovieTitleException(request.getTitle());
        }
        
        Movie movie = movieMapper.toEntity(request);
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toResponse(savedMovie);
    }

    @Override
    @Transactional
    public void updateMovie(String movieTitle, MovieRequest request) {
        Movie existingMovie = movieRepository.findByTitle(movieTitle)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "title", movieTitle));
        
        if (!movieTitle.equals(request.getTitle()) && 
            movieRepository.findByTitle(request.getTitle()).isPresent()) {
            throw new DuplicateMovieTitleException(request.getTitle());
        }
        
        movieMapper.updateEntity(existingMovie, request);
        movieRepository.save(existingMovie);
    }

    @Override
    @Transactional
    public void deleteMovie(String movieTitle) {
        Movie movie = movieRepository.findByTitle(movieTitle)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "title", movieTitle));
        movieRepository.delete(movie);
    }
} 