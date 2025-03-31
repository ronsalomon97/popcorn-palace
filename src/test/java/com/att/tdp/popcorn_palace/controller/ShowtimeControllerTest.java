package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.request.ShowtimeRequest;
import com.att.tdp.popcorn_palace.dto.response.ShowtimeResponse;
import com.att.tdp.popcorn_palace.exception.GlobalExceptionHandler;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.exception.OverlappingShowtimeException;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ShowtimeControllerTest {

    @Mock
    private ShowtimeService showtimeService;

    @InjectMocks
    private ShowtimeController showtimeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        mockMvc = MockMvcBuilders.standaloneSetup(showtimeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getShowtimeById_Success() {
        // Arrange
        Long showtimeId = 1L;
        ShowtimeResponse expectedResponse = createShowtimeResponse(
            showtimeId, "Movie Title", "Theater 1", 
            LocalDateTime.now(), LocalDateTime.now().plusHours(2), 10.0
        );
        when(showtimeService.getShowtimeById(anyLong())).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ShowtimeResponse> response = showtimeController.getShowtimeById(showtimeId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(showtimeService).getShowtimeById(showtimeId);
    }

    @Test
    void getShowtimeById_NotFound() {
        // Arrange
        Long showtimeId = 1L;
        when(showtimeService.getShowtimeById(anyLong()))
            .thenThrow(new ResourceNotFoundException("Showtime", "id", showtimeId.toString()));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> showtimeController.getShowtimeById(showtimeId));
        verify(showtimeService).getShowtimeById(showtimeId);
    }

    @Test
    void addShowtime_Success() {
        // Arrange
        ShowtimeRequest request = createShowtimeRequest(
            "Movie Title", "Theater 1", LocalDateTime.now(), 10.0
        );
        ShowtimeResponse expectedResponse = createShowtimeResponse(
            1L, "Movie Title", "Theater 1", 
            LocalDateTime.now(), LocalDateTime.now().plusHours(2), 10.0
        );
        when(showtimeService.addShowtime(any(ShowtimeRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ShowtimeResponse> response = showtimeController.addShowtime(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(showtimeService).addShowtime(request);
    }

    @Test
    void addShowtime_Overlapping() {
        // Arrange
        ShowtimeRequest request = createShowtimeRequest(
            "Movie Title", "Theater 1", LocalDateTime.now(), 10.0
        );
        when(showtimeService.addShowtime(any(ShowtimeRequest.class)))
            .thenThrow(new OverlappingShowtimeException("Theater 1"));

        // Act & Assert
        assertThrows(OverlappingShowtimeException.class, () -> showtimeController.addShowtime(request));
        verify(showtimeService).addShowtime(request);
    }

    @Test
    void updateShowtime_Success() throws Exception {
        // Arrange
        Long showtimeId = 1L;
        LocalDateTime startTime = LocalDateTime.now();
        ShowtimeRequest request = createShowtimeRequest(
            "Movie Title", "Theater 1", startTime, 10.0
        );
        doNothing().when(showtimeService).updateShowtime(anyLong(), any(ShowtimeRequest.class));

        // Act & Assert
        mockMvc.perform(post("/showtimes/update/" + showtimeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        
        verify(showtimeService).updateShowtime(eq(showtimeId), any(ShowtimeRequest.class));
    }

    @Test
    void updateShowtime_NotFound() {
        // Arrange
        Long showtimeId = 1L;
        ShowtimeRequest request = createShowtimeRequest(
            "Movie Title", "Theater 1", LocalDateTime.now(), 10.0
        );
        doThrow(new ResourceNotFoundException("Showtime", "id", showtimeId.toString()))
            .when(showtimeService).updateShowtime(anyLong(), any(ShowtimeRequest.class));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> showtimeController.updateShowtime(showtimeId, request));
        verify(showtimeService).updateShowtime(showtimeId, request);
    }

    @Test
    void deleteShowtime_Success() {
        // Arrange
        Long showtimeId = 1L;
        doNothing().when(showtimeService).deleteShowtime(anyLong());

        // Act
        ResponseEntity<Void> response = showtimeController.deleteShowtime(showtimeId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(showtimeService).deleteShowtime(showtimeId);
    }

    @Test
    void deleteShowtime_NotFound() {
        // Arrange
        Long showtimeId = 1L;
        doThrow(new ResourceNotFoundException("Showtime", "id", showtimeId.toString()))
            .when(showtimeService).deleteShowtime(anyLong());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> showtimeController.deleteShowtime(showtimeId));
        verify(showtimeService).deleteShowtime(showtimeId);
    }

    private ShowtimeRequest createShowtimeRequest(String movieTitle, String theater, LocalDateTime startTime, Double price) {
        ShowtimeRequest request = new ShowtimeRequest();
        request.setMovieTitle(movieTitle);
        request.setTheater(theater);
        request.setStartTime(startTime);
        request.setEndTime(startTime.plusHours(2));
        request.setPrice(price);
        return request;
    }

    private ShowtimeResponse createShowtimeResponse(Long id, String movieTitle, String theater, 
                                                  LocalDateTime startTime, LocalDateTime endTime, Double price) {
        ShowtimeResponse response = new ShowtimeResponse();
        response.setId(id);
        response.setMovieTitle(movieTitle);
        response.setTheater(theater);
        response.setStartTime(startTime);
        response.setEndTime(endTime);
        response.setPrice(price);
        return response;
    }
} 