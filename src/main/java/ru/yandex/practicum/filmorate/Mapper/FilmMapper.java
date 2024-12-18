package ru.yandex.practicum.filmorate.Mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .build();

        // Проверка наличия id рейтинга перед его установкой
        Integer ratingId = rs.getObject("rating_id", Integer.class);
        if (ratingId != null) {
            Rating rating = new Rating();
            rating.setId(ratingId);
            film.setRating(rating);
        }

        return film;
    }
}
