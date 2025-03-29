package com.att.tdp.popcorn_palace.mapper;

import com.att.tdp.popcorn_palace.dto.request.ShowtimeRequest;
import com.att.tdp.popcorn_palace.dto.response.ShowtimeResponse;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ShowtimeMapper {
    
    public Showtime toEntity(ShowtimeRequest request, Movie movie) {
        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setTheater(request.getTheater());
        showtime.setStartTime(request.getStartTime());
        // End time will be calculated in service layer
        showtime.setPrice(request.getPrice());
        return showtime;
    }
    
    public ShowtimeResponse toResponse(Showtime showtime) {
        ShowtimeResponse response = new ShowtimeResponse();
        response.setId(showtime.getId());
        response.setMovieTitle(showtime.getMovie().getTitle());
        response.setTheater(showtime.getTheater());
        response.setStartTime(showtime.getStartTime());
        response.setEndTime(showtime.getEndTime());
        response.setPrice(showtime.getPrice());
        return response;
    }
    
    public void updateEntity(Showtime showtime, ShowtimeRequest request, Movie movie) {
        showtime.setMovie(movie);
        showtime.setTheater(request.getTheater());
        showtime.setStartTime(request.getStartTime());
        // End time will be updated in service layer
        showtime.setPrice(request.getPrice());
    }
} 