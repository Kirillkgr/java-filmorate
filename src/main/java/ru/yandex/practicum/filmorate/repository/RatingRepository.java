package ru.yandex.practicum.filmorate.repository;


import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface RatingRepository {
    Rating save(Rating rating);
    Rating update(Rating rating);
    Rating getById(Integer id);
    List<Rating> getAll();
    void deleteById(Integer id);
    boolean existsById(Integer id);
}
