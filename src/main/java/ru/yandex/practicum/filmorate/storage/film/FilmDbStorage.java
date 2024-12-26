package ru.yandex.practicum.filmorate.storage.film;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.DTO.FilmDto;
import ru.yandex.practicum.filmorate.Enums.Genre;
import ru.yandex.practicum.filmorate.Enums.RatingMpa;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.model.GenreModel;
import ru.yandex.practicum.filmorate.repository.LikeRepository;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.Service.GenresService;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    GenresService genresService;
    private final JdbcTemplate jdbcTemplate;
    private final LikeRepository likeRepository;


    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, new FilmRowMapper());
    }


    @Override
    public Film getFilm(Integer id) {
        String sql = "SELECT * FROM films WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new FilmRowMapper(), id);
    }

    @Override
    public FilmDto updateFilm(FilmDto updateFilm) {
        if (!existsById(updateFilm.getId())) return null;
        // Обновление основных полей фильма
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, updateFilm.getName(), updateFilm.getDescription(), Date.valueOf(updateFilm.getReleaseDate()), updateFilm.getDuration(), updateFilm.getRating() != null ? updateFilm.getRating().getId() : null, updateFilm.getId());


        return updateFilm;
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM films WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count > 0;
    }


    @Override
    public Film addLike(Integer id, Integer userId) {
        String sql = "INSERT INTO FILM_LIKES (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, userId);
        return getFilm(id);
    }


    @Override
    public Film deleteLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
        return getFilm(filmId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sql = "SELECT f.*, COUNT(fl.user_id) AS likes_count " + "FROM films f LEFT JOIN film_likes fl ON f.id = fl.film_id " + "GROUP BY f.id ORDER BY likes_count DESC LIMIT ?";
        return jdbcTemplate.query(sql, new FilmRowMapper(), count);
    }

    @Override
    public Film createFilm(FilmDto newFilm) {
        try {
            if (newFilm.getGenres() != null && !newFilm.getGenres().isEmpty() && newFilm.getGenres().getFirst().getId() != null) {
                LinkedHashSet<GenreModel> genreModels = new LinkedHashSet<>();
                for (GenreModel model : newFilm.getGenres()) {
                    if (model.getId() == null) continue;

                    Genre genre = Genre.forValues(model.getId());
                    if (genre == null) {
                        log.error("Genre is null for id: {}", model.getId());
                        return null;
                    }
                    // Логирование жанра для отладки
                    System.out.println("Processing genre: " + genre);

                    // Добавляем жанр в набор genreModels
                    genreModels.add(GenreModel.builder().id(genre.getId()).name(genre.name()).build());
                }
                newFilm.setGenres(genreModels);
            }

            LinkedHashSet<GenreModel> genres = new LinkedHashSet<>();
            if (newFilm.getGenres() != null && !newFilm.getGenres().isEmpty()) {
                for (GenreModel genre : newFilm.getGenres()) {
                    if (!genresService.existsById(genre.getId())) {
                        genre = genresService.createGenre(genre);
                    }
                    genres.add(genre);
                }
                newFilm.setGenres(genres);
            }

            String sql = "INSERT INTO films (name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, newFilm.getName());
                ps.setString(2, newFilm.getDescription());
                ps.setDate(3, Date.valueOf(newFilm.getReleaseDate()));
                ps.setInt(4, newFilm.getDuration());
                ps.setInt(5, newFilm.getRating() != null ? newFilm.getRating().getId() : 0);
                return ps;
            }, keyHolder);

            if (keyHolder.getKey() != null) {
                newFilm.setId(keyHolder.getKey().intValue());
            } else {
                throw new IllegalStateException("Не удалось сгенерировать ключ для нового фильма");
            }

            saveGenres(genres, newFilm.getId());
            return convertFilmDtoToFilm(newFilm);

        } catch (Exception e) {
            log.error("Error creating film: {}", e.getMessage());
        }

        return null;
    }


    private class FilmRowMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Film film = new Film();
            film.setId(rs.getInt("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(Duration.ofMinutes(rs.getInt("duration")));
            film.setRating(RatingMpa.forValues(rs.getInt("rating_id")));

            // Получение жанров
            if (genresService.existsByFilmId(film.getId())) {
                LinkedHashSet<GenreModel> genres = genresService.getGenresByFilmId(film.getId());

                film.setGenres(genres);
            }
            // Получение Like
            if (likeRepository.existsByFilmId(film.getId()))
                film.setLikesIds(likeRepository.getFilmLikesByFilmId(film.getId()).stream().map(FilmLike::getUserId).collect(Collectors.toSet()));

            return film;
        }
    }

    public static Film convertFilmDtoToFilm(FilmDto filmDto) {
        return Film.builder().id(filmDto.getId()).name(filmDto.getName()).description(filmDto.getDescription()).releaseDate(filmDto.getReleaseDate()).duration(Duration.ofMinutes(filmDto.getDuration())).rating(filmDto.getRating()).genres(filmDto.getGenres()).build();
    }

    private void saveGenres(Set<GenreModel> genres, Integer filmId) {
        String sql = "INSERT INTO FILM_GENRES (film_id, genre_id) VALUES (?, ?)";
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                for (GenreModel genre : genres) {
                    ps.setInt(1, filmId);
                    ps.setInt(2, genre.getId());
                    ps.addBatch();
                }
                ps.executeBatch();
                connection.commit();
                log.info("Genres saved successfully for film ID {}", filmId);
            } catch (SQLException e) {
                connection.rollback();
                log.error("Error saving genres for film ID {}: {}", filmId, e.getMessage(), e);
            }
        } catch (SQLException e) {
            log.error("Error obtaining database connection: {}", e.getMessage(), e);
        }
    }


}
