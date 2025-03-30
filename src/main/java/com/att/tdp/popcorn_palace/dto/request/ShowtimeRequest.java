package com.att.tdp.popcorn_palace.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class ShowtimeRequest {
    @NotBlank(message = "Movie title is required")
    private String movieTitle;

    @NotBlank(message = "Theater is required")
    private String theater;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "end time is required")
    private LocalDateTime endTime;

    @DecimalMin(value = "0.0", message = "Price must be positive")
    private Double price;

    // Getters and Setters
    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public String getTheater() { return theater; }
    public void setTheater(String theater) { this.theater = theater; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
} 