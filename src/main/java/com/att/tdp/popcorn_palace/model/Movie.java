package com.att.tdp.popcorn_palace.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "movie")
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
} 