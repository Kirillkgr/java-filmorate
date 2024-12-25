package ru.yandex.practicum.filmorate.repository.Impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.repository.LikeRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeServiceImpl implements LikeRepository {


    JdbcTemplate jdbcTemplate;


    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM film_likes WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count > 0;
    }

    @Override
    public List<FilmLike> getFilmLikesByFilmId(Integer filmId) {
        String sql = "SELECT * FROM film_likes WHERE film_id = ?";
        return jdbcTemplate.query(sql, new FilmLikeRowMapper(), filmId);
    }

    @Override
    public boolean existsByFilmId(Integer filmId) {
        String sql = "SELECT COUNT(*) FROM film_likes WHERE film_id = ? ";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, filmId);
        return count > 0;
    }

    private static class FilmLikeRowMapper implements RowMapper<FilmLike> {
        @Override
        public FilmLike mapRow(ResultSet rs, int rowNum) throws SQLException {
            return FilmLike.builder()
                    .id(rs.getInt("id"))
                    .filmId(rs.getInt("film_id"))
                    .userId(rs.getInt("user_id"))
                    .build();
        }
    }
}

