package com.att.tdp.popcorn_palace.dto.response;

import java.time.LocalDateTime;

public class ShowtimeResponse {
    private Long id;
    private String movieTitle;
    private String theater;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double price;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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