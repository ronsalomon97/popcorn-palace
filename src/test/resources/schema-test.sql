CREATE TABLE IF NOT EXISTS movie (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    genre VARCHAR(255) NOT NULL,
    duration INTEGER NOT NULL,
    rating DOUBLE NOT NULL,
    release_year INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS showtime (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    theater VARCHAR(255) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    price DOUBLE NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie(id)
);

CREATE TABLE IF NOT EXISTS booking (
    id UUID PRIMARY KEY DEFAULT RANDOM_UUID(),
    showtime_id BIGINT NOT NULL,
    user_id UUID NOT NULL,
    seat_number INTEGER NOT NULL,
    FOREIGN KEY (showtime_id) REFERENCES showtime(id),
    UNIQUE (showtime_id, seat_number)
); 