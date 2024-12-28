package ru.yandex.practicum.filmorate.storage.Service;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Enums.Genre;
import ru.yandex.practicum.filmorate.model.GenreModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenresServiceImp implements GenresService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<GenreModel> getGenres() {
        String sql = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sql, new GenreRowMapper());
    }

    @Override
    public GenreModel createGenre(GenreModel newGenre) {
        Genre genre = newGenre.toGenre();
        String sql = "INSERT INTO genres (id, name) VALUES (?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, genre.getId());
            ps.setString(2, genre.getName());
            return ps;
        });
        return newGenre;
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM GENRES WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count > 0;
    }

    @Override
    public boolean existsByFilmId(Integer id) {
        String sql = "SELECT COUNT(*) FROM FILM_GENRES  WHERE FILM_ID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count > 0;
    }


    @Override
    public LinkedHashSet<GenreModel> getGenresByFilmId(Integer id) {
        String sql = "SELECT g.* FROM GENRES g JOIN FILM_GENRES fg ON g.id = fg.genre_id WHERE fg.film_id = ?";
        List<GenreModel> genres = jdbcTemplate.query(sql, new GenreRowMapper(), id);
        return new LinkedHashSet<>(genres);
    }

    private static class GenreRowMapper implements RowMapper<GenreModel> {
        @Override
        public GenreModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            GenreModel genre = new GenreModel();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("name"));
            return genre;
        }
    }
}
