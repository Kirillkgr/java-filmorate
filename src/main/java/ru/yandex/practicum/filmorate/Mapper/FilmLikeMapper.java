package ru.yandex.practicum.filmorate.Mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmLikeMapper implements RowMapper<FilmLike> {

    @Override
    public FilmLike mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmLike filmLike = new FilmLike();
        filmLike.setId(rs.getInt("id"));

        // Создаем объект Film и устанавливаем id
        Film film = new Film();
        film.setId(rs.getInt("film_id"));
        filmLike.setFilm(film);

        // Создаем объект User и устанавливаем id
        User user = new User();
        user.setId(rs.getInt("user_id"));
        filmLike.setUser(user);

        return filmLike;
    }
}
