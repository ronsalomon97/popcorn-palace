package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.request.MovieRequest;
import com.att.tdp.popcorn_palace.dto.response.MovieResponse;
import com.att.tdp.popcorn_palace.exception.DuplicateMovieTitleException;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllMovies_Success() {
        // Arrange
        List<MovieResponse> expectedMovies = Arrays.asList(
            createMovieResponse(1L, "Movie 1", "Action", 120, 8.5, 2024),
            createMovieResponse(2L, "Movie 2", "Comedy", 90, 7.8, 2024)
        );
        when(movieService.getAllMovies()).thenReturn(expectedMovies);

        // Act
        ResponseEntity<List<MovieResponse>> response = movieController.getAllMovies();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMovies, response.getBody());
        verify(movieService).getAllMovies();
    }

    @Test
    void addMovie_Success() {
        // Arrange
        MovieRequest request = createMovieRequest("New Movie", "Action", 120, 8.5, 2024);
        MovieResponse expectedResponse = createMovieResponse(1L, "New Movie", "Action", 120, 8.5, 2024);
        when(movieService.addMovie(any(MovieRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<MovieResponse> response = movieController.addMovie(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(movieService).addMovie(request);
    }

    @Test
    void addMovie_DuplicateTitle() {
        // Arrange
        MovieRequest request = createMovieRequest("Existing Movie", "Action", 120, 8.5, 2024);
        when(movieService.addMovie(any(MovieRequest.class)))
            .thenThrow(new DuplicateMovieTitleException("Existing Movie"));

        // Act & Assert
        assertThrows(DuplicateMovieTitleException.class, () -> movieController.addMovie(request));
        verify(movieService).addMovie(request);
    }

    @Test
    void updateMovie_Success() {
        // Arrange
        String movieTitle = "Existing Movie";
        MovieRequest request = createMovieRequest("Updated Movie", "Action", 120, 8.5, 2024);
        doNothing().when(movieService).updateMovie(anyString(), any(MovieRequest.class));

        // Act
        ResponseEntity<Void> response = movieController.updateMovie(movieTitle, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(movieService).updateMovie(movieTitle, request);
    }

    @Test
    void updateMovie_NotFound() {
        // Arrange
        String movieTitle = "Non-existent Movie";
        MovieRequest request = createMovieRequest("Updated Movie", "Action", 120, 8.5, 2024);
        doThrow(new ResourceNotFoundException("Movie", "title", movieTitle))
            .when(movieService).updateMovie(anyString(), any(MovieRequest.class));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> movieController.updateMovie(movieTitle, request));
        verify(movieService).updateMovie(movieTitle, request);
    }

    @Test
    void deleteMovie_Success() {
        // Arrange
        String movieTitle = "Movie to Delete";
        doNothing().when(movieService).deleteMovie(anyString());

        // Act
        ResponseEntity<Void> response = movieController.deleteMovie(movieTitle);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(movieService).deleteMovie(movieTitle);
    }

    @Test
    void deleteMovie_NotFound() {
        // Arrange
        String movieTitle = "Non-existent Movie";
        doThrow(new ResourceNotFoundException("Movie", "title", movieTitle))
            .when(movieService).deleteMovie(anyString());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> movieController.deleteMovie(movieTitle));
        verify(movieService).deleteMovie(movieTitle);
    }

    private MovieRequest createMovieRequest(String title, String genre, Integer duration, Double rating, Integer releaseYear) {
        MovieRequest request = new MovieRequest();
        request.setTitle(title);
        request.setGenre(genre);
        request.setDuration(duration);
        request.setRating(rating);
        request.setReleaseYear(releaseYear);
        return request;
    }

    private MovieResponse createMovieResponse(Long id, String title, String genre, Integer duration, Double rating, Integer releaseYear) {
        MovieResponse response = new MovieResponse();
        response.setId(id);
        response.setTitle(title);
        response.setGenre(genre);
        response.setDuration(duration);
        response.setRating(rating);
        response.setReleaseYear(releaseYear);
        return response;
    }
} 