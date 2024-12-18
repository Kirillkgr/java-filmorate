package ru.yandex.practicum.filmorate.repository.Impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.RatingRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmServiceImpl implements FilmRepository {
    private JdbcTemplate jdbcTemplate;
    private RatingRepository ratingRepository;

    @Override
    public Film save(Film film) {
        Rating rating = null;
        if (film.getRating() == null) {
            rating = ratingRepository.save(Rating.builder().description("").name("Не указан").build());
            film.setRating(rating);
        }
        if (film.getRating() == null) {
            throw new IllegalArgumentException("Rating cannot be null");
        }
        String sql = "INSERT INTO films (name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getRating().getId());
            return ps;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());
        return film;
    }


    @Override
    public Boolean exists(int id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM films WHERE id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public Boolean exists() {
        String sql = "SELECT EXISTS(SELECT 1 FROM films)";
        return jdbcTemplate.queryForObject(sql, Boolean.class);
    }


    @Override
    public Film update(Film film) {
        if (!exists(film.getId())) return null;
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getRating().getId(), film.getId());
        // Получаем обновленный фильм из базы данных
        return get(film.getId());
    }

    @Override
    public Film get(Integer id) {
        if (!exists(id)) return null;
        String sql = "SELECT * FROM films WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new FilmMapper(), id);
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT * FROM films";
        return jdbcTemplate.query(sql, new FilmMapper());
    }

    @Override
    public List<Film> findAllById(Set<Integer> filmIds) {
        List<Film> completeFilms = new ArrayList<>();
        if (!filmIds.isEmpty()) {
            for (Integer id : filmIds) {
                completeFilms.add(get(id));
            }
            log.info("Получен список фильмов в количестве: {}", completeFilms.size());
            return completeFilms;
        }
        log.warn("Фильмы отсутствуют");
        return new ArrayList<>();
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM films WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Film> getAllById(List<Integer> filmIds) {
        if (filmIds == null || filmIds.isEmpty()) {
            return List.of();
        }
        String sql = "SELECT * FROM films WHERE id IN (" +
                filmIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", ")) + ")";

        return jdbcTemplate.query(sql, new FilmMapper());
    }

}



