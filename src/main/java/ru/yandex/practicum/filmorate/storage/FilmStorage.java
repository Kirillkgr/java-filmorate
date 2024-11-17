package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import ru.yandex.practicum.filmorate.DTO.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
	List<Film> getFilms();

	Film getFilm(Integer id);

	FilmDto updateFilm(FilmDto updateFilm);

	FilmDto createFilm(FilmDto newFilm);
}
