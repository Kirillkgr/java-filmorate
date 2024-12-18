CREATE TABLE ratings (
                         id INT PRIMARY KEY,
                         name VARCHAR(50) NOT NULL,
                         description TEXT
);

CREATE TABLE genres (
                        id INT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL
);

CREATE TABLE users (
                       id INT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       login VARCHAR(50) NOT NULL UNIQUE,
                       birthday DATE NOT NULL
);

CREATE TABLE films (
                       id INT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description VARCHAR(200) NOT NULL,
                       release_date DATE NOT NULL,
                       duration INT NOT NULL,
                       rating_id INT,
                       FOREIGN KEY (rating_id) REFERENCES ratings(id)
);

CREATE TABLE film_genres (
                             id INT PRIMARY KEY,
                             film_id INT,
                             genre_id INT,
                             FOREIGN KEY (film_id) REFERENCES films(id),
                             FOREIGN KEY (genre_id) REFERENCES genres(id)
);

CREATE TABLE film_likes (
                            id INT PRIMARY KEY,
                            film_id INT,
                            user_id INT,
                            FOREIGN KEY (film_id) REFERENCES films(id),
                            FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE friendship (
                            id INT PRIMARY KEY,
                            user_id INT,
                            friend_id INT,
                            status VARCHAR(50) NOT NULL,
                            FOREIGN KEY (user_id) REFERENCES users(id),
                            FOREIGN KEY (friend_id) REFERENCES users(id)
);
