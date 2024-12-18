package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmLikeRepository {
    FilmLike save(FilmLike filmLike);

    Boolean exists(int id);

    Boolean exists();

    FilmLike update(FilmLike filmLike);

    FilmLike get(Integer id);

    List<FilmLike> getAll();

    List<FilmLike> findAllById(Set<Integer> likeIds);

    void delete(Integer id);

    void delete(Integer id, Integer userId);

    List<Map<String, Object>> findPopularFilms(int count);
}
