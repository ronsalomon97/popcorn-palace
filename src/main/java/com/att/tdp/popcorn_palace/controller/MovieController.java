package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.request.MovieRequest;
import com.att.tdp.popcorn_palace.dto.response.MovieResponse;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<MovieResponse>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }
    
    @PostMapping
    public ResponseEntity<MovieResponse> addMovie(@Valid @RequestBody MovieRequest request) {
        return ResponseEntity.ok(movieService.addMovie(request));
    }
    
    @PostMapping("/update/{movieTitle}")
    public ResponseEntity<Void> updateMovie(
            @PathVariable String movieTitle,
            @Valid @RequestBody MovieRequest request) {
        movieService.updateMovie(movieTitle, request);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{movieTitle}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String movieTitle) {
        movieService.deleteMovie(movieTitle);
        return ResponseEntity.ok().build();
    }
} 