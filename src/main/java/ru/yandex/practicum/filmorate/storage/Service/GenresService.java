package ru.yandex.practicum.filmorate.storage.Service;

import ru.yandex.practicum.filmorate.model.GenreModel;

import java.util.LinkedHashSet;
import java.util.List;

public interface GenresService {

    List<GenreModel> getGenres();

    GenreModel createGenre(GenreModel newGenre);

    boolean existsById(Integer id);

    boolean existsByFilmId(Integer id);

    LinkedHashSet<GenreModel> getGenresByFilmId(Integer id);
}
