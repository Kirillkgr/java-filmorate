-- Удаление таблиц с учетом зависимостей
DROP TABLE IF EXISTS film_genres;
DROP TABLE IF EXISTS film_likes;
DROP TABLE IF EXISTS ratings;
DROP TABLE IF EXISTS friendship;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS genres;

-- Создание таблиц
CREATE TABLE genres
(
    id   INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE films
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255),
    description  TEXT,
    release_date DATE,
    duration     INT,
    rating_id    INT
);

CREATE TABLE IF NOT EXISTS rating_mpa
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255),
    description VARCHAR(255)
);

create table FILM_LIKES
(
    ID      INTEGER not null auto_increment,
    FILM_ID INTEGER,
    USER_ID INTEGER
);

CREATE TABLE users
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(255),
    email    VARCHAR(255),
    login    VARCHAR(255),
    birthday DATE
);

CREATE TABLE FRIENDSHIP
(
    ID        INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
    USER_ID   INTEGER,
    FRIEND_ID INTEGER,
    STATUS    CHARACTER VARYING(50)  NOT NULL
);


CREATE TABLE IF NOT EXISTS film_genres
(
    film_id  INTEGER REFERENCES films (id) ON DELETE CASCADE,
    genre_id INTEGER REFERENCES genres (id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);
