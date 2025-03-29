package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.request.MovieRequest;
import com.att.tdp.popcorn_palace.dto.response.MovieResponse;
import com.att.tdp.popcorn_palace.model.Movie;
import java.util.List;

public interface MovieService {
    List<MovieResponse> getAllMovies();
    MovieResponse addMovie(MovieRequest request);
    void updateMovie(String movieTitle, MovieRequest request);
    void deleteMovie(String movieTitle);
} 