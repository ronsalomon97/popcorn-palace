INSERT INTO movie (title, genre, duration, rating, release_year) VALUES
    ('The Shawshank Redemption', 'Drama', 142, 9.3, 1994),
    ('The Godfather', 'Crime Drama', 175, 9.2, 1972),
    ('Inception', 'Sci-Fi', 148, 8.8, 2010);

INSERT INTO showtime (movie_id, price, theater, start_time, end_time) VALUES
    (1, 50.20, 'Theater 1', '2025-02-14 11:00:00', '2025-02-14 13:22:00'),
    (2, 45.50, 'Theater 2', '2025-02-14 14:00:00', '2025-02-14 16:55:00'),
    (3, 55.00, 'Theater 3', '2025-02-14 17:00:00', '2025-02-14 19:28:00');