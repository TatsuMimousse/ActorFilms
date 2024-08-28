CREATE DATABASE Movies;

-- Create actors table
CREATE TABLE actors (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    birthdate DATE NOT NULL
);

-- Insert actor records
INSERT INTO actors (id, name, surname, birthdate) VALUES
(1, 'Leonardo', 'DiCaprio', '1974-11-11'),
(2, 'Tom', 'Ellis', '1981-02-17'),
(3, 'Brendan', 'Fraser', '1968-12-03'),
(4, 'Tom', 'Hardy', '1977-09-15');


-- Create movies table

CREATE TABLE movies (
    id INT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
	duration VARCHAR(50) NOT NULL,
    genre VARCHAR(255) NOT NULL,
);


NSERT INTO movies (id, title, duration, genre) VALUES
(1, 'Inception', '02:28:00', 'Sci-Fi'),
(2, 'Lucifer', '03:00:00', 'Drama'),
(3, 'The Mummy', '02:04:00', 'Adventure'),
(4, 'Mad Max: Fury Road', '02:00:00', 'Action');

CREATE TABLE movies_actors (
    movies_id INT,
    actor_id INT,
    character_name VARCHAR(255),
    PRIMARY KEY (movies_id, actor_id),
    FOREIGN KEY (movies_id) REFERENCES movies(id),
    FOREIGN KEY (actor_id) REFERENCES actors(id)
);

INSERT INTO movies_actors (movies_id, actor_id, character_name) VALUES
(1, 1, 'Dom Cobb'),
(2, 2, 'Lucifer Morningstar'), 
(3, 3, 'Rick O''Connell'),
(4, 4, 'Max Rockatansky'); 


