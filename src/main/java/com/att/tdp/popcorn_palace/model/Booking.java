package com.att.tdp.popcorn_palace.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Data
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;
} 