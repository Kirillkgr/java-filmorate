package ru.yandex.practicum.filmorate.repository.Impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;
import java.util.Set;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenresRepository;

@Repository
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenresServiceImpl implements GenresRepository {

    @Override
    public Set<Genre> getAllGenres() {
        Set<Genre> genres = new LinkedHashSet<>();
        for (int i = 1; i <= Genre.values().length; i++) {
            genres.add(Genre.forValues(i));
        }
        return genres;
    }

    @Override
    public Genre getGenre(Integer id) {
        return switch (id) {
            case 1 -> Genre.COMEDY;
            case 2 -> Genre.DRAMA;
            case 3 -> Genre.CARTOON;
            case 4 -> Genre.THRILLER;
            case 5 -> Genre.DOCUMENTARY;
            case 6 -> Genre.ACTION;
            default -> null;
        };
    }
}



