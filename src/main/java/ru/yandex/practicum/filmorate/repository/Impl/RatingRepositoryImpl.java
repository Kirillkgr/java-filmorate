package ru.yandex.practicum.filmorate.repository.Impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Mapper.RatingMapper;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.RatingRepository;

import java.sql.PreparedStatement;
import java.util.List;


@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RatingRepositoryImpl implements RatingRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Rating save(Rating rating) {
        String sql = "INSERT INTO ratings (name, description) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, rating.getName());
            ps.setString(2, rating.getDescription());
            return ps;
        }, keyHolder);

        rating.setId(keyHolder.getKey().intValue());
        return rating;
    }

    @Override
    public Rating update(Rating rating) {
        String sql = "UPDATE ratings SET name = ?, description = ? WHERE id = ?";
        jdbcTemplate.update(sql, rating.getName(), rating.getDescription(), rating.getId());
        return rating;
    }

    @Override
    public Rating getById(Integer id) {
        String sql = "SELECT * FROM ratings WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new RatingMapper(), id);
    }

    @Override
    public List<Rating> getAll() {
        String sql = "SELECT * FROM ratings";
        return jdbcTemplate.query(sql, new RatingMapper());
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM ratings WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM ratings WHERE id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }
}
