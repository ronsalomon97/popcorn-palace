package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.request.ShowtimeRequest;
import com.att.tdp.popcorn_palace.dto.response.ShowtimeResponse;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @Autowired
    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowtimeResponse> getShowtimeById(@PathVariable Long id) {
        return ResponseEntity.ok(showtimeService.getShowtimeById(id));
    }

    @PostMapping
    public ResponseEntity<ShowtimeResponse> addShowtime(@Valid @RequestBody ShowtimeRequest request) {
        return ResponseEntity.ok(showtimeService.addShowtime(request));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Void> updateShowtime(
            @PathVariable Long id,
            @Valid @RequestBody ShowtimeRequest request) {
        showtimeService.updateShowtime(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtime(id);
        return ResponseEntity.ok().build();
    }
} 