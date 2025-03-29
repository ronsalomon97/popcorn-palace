DROP TABLE IF EXISTS booking;
DROP TABLE IF EXISTS showtime;
DROP TABLE IF EXISTS movie;

CREATE TABLE IF NOT EXISTS movie (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(100) NOT NULL,
    duration INTEGER NOT NULL,
    rating DECIMAL(3,1) NOT NULL,
    release_year INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS showtime (
    id SERIAL PRIMARY KEY,
    movie_id INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    theater VARCHAR(100) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie(id)
);

CREATE TABLE IF NOT EXISTS booking (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    showtime_id INTEGER NOT NULL,
    user_id UUID NOT NULL,
    seat_number INTEGER NOT NULL,
    FOREIGN KEY (showtime_id) REFERENCES showtime(id),
    UNIQUE (showtime_id, seat_number)
);