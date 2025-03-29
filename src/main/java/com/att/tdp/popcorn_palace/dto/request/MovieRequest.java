package com.att.tdp.popcorn_palace.dto.request;

import jakarta.validation.constraints.*;
import java.time.Year;

public class MovieRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Genre is required")
    private String genre;

    @Min(value = 1, message = "Duration must be positive")
    @Max(value = 1000, message = "Duration must not exceed 1000 minutes")
    private Integer duration;

    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "10.0", message = "Rating must not exceed 10.0")
    private Double rating;

    @Min(value = 1888, message = "Release year must be after the first movie was made (1888)")
    @Max(value = 2100, message = "Release year must be reasonable")
    private Integer releaseYear;

    // Default constructor
    public MovieRequest() {}

    // Getters
    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getDuration() {
        return duration;
    }

    public Double getRating() {
        return rating;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }
} 