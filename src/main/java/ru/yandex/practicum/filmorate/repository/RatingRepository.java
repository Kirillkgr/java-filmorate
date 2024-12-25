package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;

public interface RatingRepository {
    RatingMpa findById(int id);

    List<RatingMpa> findAll();
}
