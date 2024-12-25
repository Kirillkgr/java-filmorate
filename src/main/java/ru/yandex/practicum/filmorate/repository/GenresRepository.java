package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenresRepository {
    Set<Genre> getAllGenres();

    Genre getGenre(Integer id);
}
