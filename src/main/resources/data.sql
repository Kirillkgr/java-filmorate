-- Данные для таблицы ratings
INSERT INTO ratings (id, name, description) VALUES
                                                (1, 'G', 'General audiences – All ages admitted.'),
                                                (2, 'PG', 'Parental guidance suggested – Some material may not be suitable for children.'),
                                                (3, 'PG-13', 'Parents strongly cautioned – Some material may be inappropriate for children under 13.'),
                                                (4, 'R', 'Restricted – Under 17 requires accompanying parent or adult guardian.'),
                                                (5, 'NC-17', 'Adults Only – No one 17 and under admitted.');

-- Данные для таблицы genres
INSERT INTO genres (id, name) VALUES
                                  (1, 'Action'),
                                  (2, 'Comedy'),
                                  (3, 'Drama'),
                                  (4, 'Horror'),
                                  (5, 'Science Fiction');

-- Данные для таблицы films
INSERT INTO films (id, name, description, release_date, duration, rating_id) VALUES
                                                                                 (1, 'The Matrix', 'A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.', '1999-03-31', 136, 5),
                                                                                 (2, 'Inception', 'A thief who steals corporate secrets through use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.', '2010-07-16', 148, 5),
                                                                                 (3, 'The Godfather', 'The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.', '1972-03-24', 175, 4),
                                                                                 (4, 'Toy Story', 'A cowboy doll is profoundly threatened and jealous when a new spaceman figure supplants him as top toy in a boy''s room.', '1995-11-22', 81, 1),
                                                                                 (5, 'Finding Nemo', 'After his son is captured in the Great Barrier Reef and taken to Sydney, a timid clownfish sets out on a journey to bring him home.', '2003-05-30', 100, 1);

-- Данные для таблицы film_genres
INSERT INTO film_genres (id, film_id, genre_id) VALUES
                                                    (1, 1, 1),  -- The Matrix - Action
                                                    (2, 2, 1),  -- Inception - Action
                                                    (3, 2, 5),  -- Inception - Science Fiction
                                                    (4, 3, 3),  -- The Godfather - Drama
                                                    (5, 4, 2),  -- Toy Story - Comedy
                                                    (6, 5, 2),  -- Finding Nemo - Comedy
                                                    (7, 5, 3);  -- Finding Nemo - Drama

-- Данные для таблицы users
INSERT INTO users (id, name, email, login, birthday) VALUES
                                                         (1, 'Alice', 'alice@example.com', 'alice', '1985-01-15'),
                                                         (2, 'Bob', 'bob@example.com', 'bob', '1990-03-22'),
                                                         (3, 'Charlie', 'charlie@example.com', 'charlie', '1988-07-30'),
                                                         (4, 'David', 'david@example.com', 'david', '1992-05-14'),
                                                         (5, 'Eve', 'eve@example.com', 'eve', '1986-11-20');

-- Данные для таблицы film_likes
INSERT INTO film_likes (id, film_id, user_id) VALUES
                                                  (1, 1, 1),  -- Alice likes The Matrix
                                                  (2, 2, 1),  -- Alice likes Inception
                                                  (3, 3, 2),  -- Bob likes The Godfather
                                                  (4, 4, 3),  -- Charlie likes Toy Story
                                                  (5, 5, 4);  -- David likes Finding Nemo

-- Данные для таблицы friendship
INSERT INTO friendship (id, user_id, friend_id, status) VALUES
                                                            (1, 1, 2, 'confirmed'),  -- Alice and Bob are friends
                                                            (2, 2, 3, 'confirmed'),  -- Bob and Charlie are friends
                                                            (3, 3, 4, 'confirmed'),  -- Charlie and David are friends
                                                            (4, 4, 5, 'confirmed'),  -- David and Eve are friends
                                                            (5, 1, 5, 'unconfirmed');  -- Alice sent a friend request to Eve
