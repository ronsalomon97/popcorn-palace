package com.att.tdp.popcorn_palace.exception;

public class MovieHasShowtimesException extends RuntimeException {
    
    private final String movieTitle;
    
    public MovieHasShowtimesException(String movieTitle) {
        super("Cannot delete movie '" + movieTitle + "' because it has associated showtimes. Please delete all showtimes for this movie first.");
        this.movieTitle = movieTitle;
    }
    
    public String getMovieTitle() {
        return movieTitle;
    }
} 