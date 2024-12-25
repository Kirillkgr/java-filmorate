package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.List;

public interface LikeRepository {

    boolean existsById(Integer id);

    List<FilmLike> getFilmLikesByFilmId(Integer filmId);

    boolean existsByFilmId(Integer filmId);

}



