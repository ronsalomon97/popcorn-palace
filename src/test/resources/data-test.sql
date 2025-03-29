-- Insert sample movies
INSERT INTO movie (title, genre, duration, rating, release_year) VALUES
('The Matrix', 'Action', 136, 8.7, 1999),
('Inception', 'Sci-Fi', 148, 8.8, 2010),
('The Dark Knight', 'Action', 152, 9.0, 2008);

-- Insert sample showtimes
INSERT INTO showtime (movie_id, theater, start_time, end_time, price) VALUES
(1, 'Theater 1', '2024-03-20 10:00:00', '2024-03-20 12:16:00', 12.99),
(2, 'Theater 2', '2024-03-20 13:00:00', '2024-03-20 15:28:00', 14.99),
(3, 'Theater 1', '2024-03-20 16:00:00', '2024-03-20 18:32:00', 15.99); 