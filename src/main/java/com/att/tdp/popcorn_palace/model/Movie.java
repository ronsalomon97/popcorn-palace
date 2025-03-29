package com.att.tdp.popcorn_palace.model;

import jakarta.persistence.*;

@Entity
@Table(name = "movie", uniqueConstraints = {
    @UniqueConstraint(columnNames = "title", name = "uk_movie_title")
})
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String genre;
    
    @Column(nullable = false)
    private Integer duration;
    
    @Column(nullable = false)
    private Double rating;
    
    @Column(name = "release_year", nullable = false)
    private Integer releaseYear;

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public Integer getDuration() { return duration; }
    public Double getRating() { return rating; }
    public Integer getReleaseYear() { return releaseYear; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public void setRating(Double rating) { this.rating = rating; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }
} 