package ru.yandex.practicum.filmorate.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmRepository {
    Film save(Film film);

    Boolean exists(int id);

    Boolean exists();

    Film update(Film film);

    Film get(Integer id);

    List<Film> getAll();

    List<Film> findAllById(Set<Integer> filmIds);

    void delete(Integer id);

    List<Film> getAllById(List<Integer> filmIds);
}
