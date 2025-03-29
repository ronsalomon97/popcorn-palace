package com.att.tdp.popcorn_palace.mapper;

import com.att.tdp.popcorn_palace.dto.request.MovieRequest;
import com.att.tdp.popcorn_palace.dto.response.MovieResponse;
import com.att.tdp.popcorn_palace.model.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {
    
    public Movie toEntity(MovieRequest request) {
        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setGenre(request.getGenre());
        movie.setDuration(request.getDuration());
        movie.setRating(request.getRating());
        movie.setReleaseYear(request.getReleaseYear());
        return movie;
    }
    
    public MovieResponse toResponse(Movie movie) {
        MovieResponse response = new MovieResponse();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        response.setGenre(movie.getGenre());
        response.setDuration(movie.getDuration());
        response.setRating(movie.getRating());
        response.setReleaseYear(movie.getReleaseYear());
        return response;
    }
    
    public void updateEntity(Movie movie, MovieRequest request) {
        movie.setTitle(request.getTitle());
        movie.setGenre(request.getGenre());
        movie.setDuration(request.getDuration());
        movie.setRating(request.getRating());
        movie.setReleaseYear(request.getReleaseYear());
    }
} 