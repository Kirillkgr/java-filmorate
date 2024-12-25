package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import ru.yandex.practicum.filmorate.DTO.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
	List<Film> getFilms();

	Film getFilm(Integer id);

	FilmDto updateFilm(FilmDto updateFilm);

	boolean existsById(Integer id);

	Film addLike(Integer id, Integer userId);

	Film deleteLike(Integer filmId, Integer userId);

	List<Film> getPopularFilms(Integer count);

	Film createFilm(FilmDto newFilm);
}
