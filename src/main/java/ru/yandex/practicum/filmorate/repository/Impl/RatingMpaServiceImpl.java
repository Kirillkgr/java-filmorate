package ru.yandex.practicum.filmorate.repository.Impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.repository.RatingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingMpaServiceImpl implements RatingRepository {

    @Override
    public RatingMpa findById(int id) {
        return RatingMpa.forValues(id);
    }

    @Override
    public List<RatingMpa> findAll() {
        List<RatingMpa> ratingMpa = new ArrayList<>();
        for (int i = 0; i < RatingMpa.values().length; i++) {
            ratingMpa.add(RatingMpa.forValues(i + 1));
        }
        return ratingMpa;
    }
}
