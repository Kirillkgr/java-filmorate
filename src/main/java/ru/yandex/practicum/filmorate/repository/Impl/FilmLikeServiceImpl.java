package ru.yandex.practicum.filmorate.repository.Impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.repository.FilmLikeRepository;
import ru.yandex.practicum.filmorate.Mapper.FilmLikeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmLikeServiceImpl implements FilmLikeRepository {
    private JdbcTemplate jdbcTemplate;

    @Override
    public FilmLike save(FilmLike filmLike) {
        String sql = "INSERT INTO film_likes (id, film_id, user_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, filmLike.getId(), filmLike.getFilm().getId(), filmLike.getUser().getId());
        return filmLike;
    }

    @Override
    public Boolean exists(int id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM film_likes WHERE id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public Boolean exists() {
        String sql = "SELECT EXISTS(SELECT 1 FROM film_likes)";
        return jdbcTemplate.queryForObject(sql, Boolean.class);
    }

    @Override
    public FilmLike update(FilmLike filmLike) {
        if (!exists(filmLike.getId())) return null;
        String sql = "UPDATE film_likes SET film_id = ?, user_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, filmLike.getFilm().getId(), filmLike.getUser().getId(), filmLike.getId());
        // Получаем обновленный лайк из базы данных
        return get(filmLike.getId());
    }

    @Override
    public FilmLike get(Integer id) {
        if (!exists(id)) return null;
        String sql = "SELECT * FROM film_likes WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new FilmLikeMapper(), id);
    }

    @Override
    public List<FilmLike> getAll() {
        String sql = "SELECT * FROM film_likes";
        return jdbcTemplate.query(sql, new FilmLikeMapper());
    }

    @Override
    public List<FilmLike> findAllById(Set<Integer> likeIds) {
        List<FilmLike> completeLikes = new ArrayList<>();
        if (!likeIds.isEmpty()) {
            for (Integer id : likeIds) {
                completeLikes.add(get(id));
            }
            log.info("Получен список лайков в количестве: {}", completeLikes.size());
            return completeLikes;
        }
        log.warn("Лайки отсутствуют");
        return new ArrayList<>();
    }


    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM film_likes WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void delete(Integer id, Integer userId) {
        String sql = "DELETE FROM film_likes WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Map<String, Object>> findPopularFilms(int count) {
        String sql = "SELECT film_id, COUNT(*) AS likes " +
                "FROM film_likes " +
                "GROUP BY film_id " +
                "ORDER BY likes DESC " +
                "LIMIT ?";
        return jdbcTemplate.queryForList(sql, count);
    }
}
