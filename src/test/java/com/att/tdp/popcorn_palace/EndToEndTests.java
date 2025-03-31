package com.att.tdp.popcorn_palace;

import com.att.tdp.popcorn_palace.config.TestConfig;
import com.att.tdp.popcorn_palace.dto.request.BookingRequest;
import com.att.tdp.popcorn_palace.dto.request.MovieRequest;
import com.att.tdp.popcorn_palace.dto.request.ShowtimeRequest;
import com.att.tdp.popcorn_palace.dto.response.BookingResponse;
import com.att.tdp.popcorn_palace.dto.response.MovieResponse;
import com.att.tdp.popcorn_palace.dto.response.ShowtimeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
@TestPropertySource(locations = "classpath:application-test.yaml")
@ActiveProfiles("test")
class EndToEndTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MovieResponse testMovie;
    private ShowtimeResponse testShowtime;

    @BeforeEach
    void setUp() throws Exception {
        // Clean up database before each test
        cleanDatabase();

        String uniqueTitle = "Test Movie";
        
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setTitle(uniqueTitle);
        movieRequest.setGenre("Test Genre");
        movieRequest.setDuration(120);
        movieRequest.setRating(8.5);
        movieRequest.setReleaseYear(2024);

        MvcResult movieResult = mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieRequest)))
                .andExpect(status().isOk())
                .andReturn();

        testMovie = objectMapper.readValue(movieResult.getResponse().getContentAsString(), MovieResponse.class);

        // Create a test showtime
        ShowtimeRequest showtimeRequest = new ShowtimeRequest();
        showtimeRequest.setMovieTitle(uniqueTitle); // Use the same movie title
        showtimeRequest.setTheater("Test Theater");
        showtimeRequest.setStartTime(LocalDateTime.now().plusDays(1));
        showtimeRequest.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        showtimeRequest.setPrice(15.99);

        MvcResult showtimeResult = mockMvc.perform(post("/showtimes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(showtimeRequest)))
                .andExpect(status().isOk())
                .andReturn();

        testShowtime = objectMapper.readValue(showtimeResult.getResponse().getContentAsString(), ShowtimeResponse.class);
    }
    
    /**
     * Cleans the database by truncating all tables
     */
    private void cleanDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE booking");
        jdbcTemplate.execute("TRUNCATE TABLE showtime");
        jdbcTemplate.execute("TRUNCATE TABLE movie");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    // Movies API Tests
    @Test
    void getAllMovies_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/movies/all"))
                .andExpect(status().isOk())
                .andReturn();

        List<MovieResponse> movies = objectMapper.readValue(result.getResponse().getContentAsString(), 
                objectMapper.getTypeFactory().constructCollectionType(List.class, MovieResponse.class));
        assertFalse(movies.isEmpty());
    }

    @Test
    void addMovie_Success() throws Exception {
        MovieRequest request = new MovieRequest();
        request.setTitle("New Movie");
        request.setGenre("Action");
        request.setDuration(150);
        request.setRating(9.0);
        request.setReleaseYear(2024);

        MvcResult result = mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        MovieResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), MovieResponse.class);
        assertEquals("New Movie", response.getTitle());
    }

    @Test
    void addMovie_InvalidData() throws Exception {
        MovieRequest request = new MovieRequest();
        request.setTitle(""); // Invalid empty title
        request.setGenre("Action");
        request.setDuration(150);
        request.setRating(9.0);
        request.setReleaseYear(2024);

        mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateMovie_Success() throws Exception {
        MovieRequest request = new MovieRequest();
        request.setTitle("Updated Movie");
        request.setGenre("Drama");
        request.setDuration(160);
        request.setRating(8.8);
        request.setReleaseYear(2024);

        mockMvc.perform(post("/movies/update/Test Movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void updateMovie_NonExistent() throws Exception {
        MovieRequest request = new MovieRequest();
        request.setTitle("Updated Movie");
        request.setGenre("Drama");
        request.setDuration(160);
        request.setRating(8.8);
        request.setReleaseYear(2024);

        mockMvc.perform(post("/movies/update/NonExistentMovie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMovie_Success() throws Exception {
        // First, delete the showtime that references this movie
        mockMvc.perform(delete("/showtimes/" + testShowtime.getId()))
                .andExpect(status().isOk());
        
        // Now delete the movie
        mockMvc.perform(delete("/movies/Test Movie"))
                .andExpect(status().isOk());
        
        // Verify the movie is actually deleted
        MvcResult verifyResult = mockMvc.perform(get("/movies/all"))
                .andExpect(status().isOk())
                .andReturn();
                
        List<MovieResponse> remainingMovies = objectMapper.readValue(verifyResult.getResponse().getContentAsString(), 
                objectMapper.getTypeFactory().constructCollectionType(List.class, MovieResponse.class));
        
        boolean movieStillExists = remainingMovies.stream()
                .anyMatch(movie -> movie.getTitle().equals("Test Movie"));
        
        assertFalse(movieStillExists, "Movie should have been deleted");
    }

    @Test
    void deleteMovie_NonExistent() throws Exception {
        mockMvc.perform(delete("/movies/NonExistentMovie"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMovie_WithShowtimes_ReturnConflict() throws Exception {
        // Try to delete a movie that still has showtimes
        MvcResult result = mockMvc.perform(delete("/movies/Test Movie"))
                .andExpect(status().isConflict())
                .andReturn();
        
        // Verify the error message
        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Cannot delete movie 'Test Movie' because it has associated showtimes"));
        
        // Verify the movie still exists
        MvcResult verifyResult = mockMvc.perform(get("/movies/all"))
                .andExpect(status().isOk())
                .andReturn();
        
        List<MovieResponse> remainingMovies = objectMapper.readValue(verifyResult.getResponse().getContentAsString(), 
                objectMapper.getTypeFactory().constructCollectionType(List.class, MovieResponse.class));
        
        boolean movieStillExists = remainingMovies.stream()
                .anyMatch(movie -> movie.getTitle().equals("Test Movie"));
        
        assertTrue(movieStillExists, "Movie should still exist");
    }

    // Showtimes API Tests
    @Test
    void getShowtimeById_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/showtimes/" + testShowtime.getId()))
                .andExpect(status().isOk())
                .andReturn();

        ShowtimeResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ShowtimeResponse.class);
        assertEquals(testShowtime.getId(), response.getId());
    }

    @Test
    void addShowtime_Success() throws Exception {
        ShowtimeRequest request = new ShowtimeRequest();
        request.setTheater("New Theater");
        request.setMovieTitle(testMovie.getTitle());
        request.setStartTime(LocalDateTime.now().plusDays(2));
        request.setEndTime(LocalDateTime.now().plusDays(2).plusHours(2));
        request.setPrice(16.99);

        MvcResult result = mockMvc.perform(post("/showtimes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        ShowtimeResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), ShowtimeResponse.class);
        assertEquals("New Theater", response.getTheater());
    }

    @Test
    void updateShowtime_Success() throws Exception {
        ShowtimeRequest request = new ShowtimeRequest();
        request.setTheater("Updated Theater");
        request.setMovieTitle(testMovie.getTitle());
        request.setStartTime(LocalDateTime.now().plusDays(3));
        request.setEndTime(LocalDateTime.now().plusDays(3).plusHours(2));
        request.setPrice(17.99);

        mockMvc.perform(post("/showtimes/update/" + testShowtime.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void updateShowtime_NonExistent() throws Exception {
        ShowtimeRequest request = new ShowtimeRequest();
        request.setTheater("Updated Theater");
        request.setMovieTitle(testMovie.getTitle());
        request.setStartTime(LocalDateTime.now().plusDays(3));
        request.setEndTime(LocalDateTime.now().plusDays(3).plusHours(2));
        request.setPrice(17.99);

        mockMvc.perform(post("/showtimes/update/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteShowtime_Success() throws Exception {
        mockMvc.perform(delete("/showtimes/" + testShowtime.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteShowtime_NonExistent() throws Exception {
        mockMvc.perform(delete("/showtimes/999999"))
                .andExpect(status().isNotFound());
    }

    // Bookings API Tests
    @Test
    void createBooking_Success() throws Exception {
        BookingRequest request = new BookingRequest();
        request.setShowtimeId(testShowtime.getId());
        request.setUserId(UUID.randomUUID());
        request.setSeatNumber(1);

        MvcResult result = mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        BookingResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), BookingResponse.class);
        assertNotNull(response.getBookingId());
    }

    @Test
    void createBooking_InvalidShowtime() throws Exception {
        BookingRequest request = new BookingRequest();
        request.setShowtimeId(999999L); // Non-existent showtime ID
        request.setUserId(UUID.randomUUID());
        request.setSeatNumber(1);

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBooking_InvalidSeatNumber() throws Exception {
        BookingRequest request = new BookingRequest();
        request.setShowtimeId(testShowtime.getId());
        request.setUserId(UUID.randomUUID());
        request.setSeatNumber(-1); // Invalid seat number

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
} 