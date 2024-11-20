package ru.yandex.practicum.filmorate.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.DTO.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import static ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage.convertFilmToFilmDto;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/films")
public class FilmController {
	final FilmStorage filmStorage;


	@GetMapping
	public ResponseEntity<List<Film>> getFilms() {
		List<Film> filmCollection = filmStorage.getFilms();
		if (filmCollection != null && !filmCollection.isEmpty()) {
			return ResponseEntity.ok(new ArrayList<>(filmCollection));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Film> getFilm(@PathVariable Integer id) {
		Film filmCollection = filmStorage.getFilm(id);
		if (filmCollection != null) {
			return ResponseEntity.ok(filmCollection);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@PutMapping
	public ResponseEntity<FilmDto> updateFilm(@Validated @RequestBody FilmDto updateFilm) {
		FilmDto filmDto = filmStorage.updateFilm(updateFilm);
		if (filmDto == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(updateFilm);
		}
		return ResponseEntity.ok(filmDto);
	}

	@PostMapping
	public ResponseEntity<FilmDto> createFilm(@Validated @RequestBody FilmDto newFilm) {
		Film film = filmStorage.createFilm(newFilm);
		newFilm = convertFilmToFilmDto(film);
		return ResponseEntity.ok(newFilm);
	}

	// new
	@PutMapping(value = "/{filmId}/like/{userId}")
	public ResponseEntity<?> addLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
		Film film = filmStorage.addLike(filmId, userId);
		if (film == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", "Film with or user not found"));
		}
		return ResponseEntity.ok(film);
	}

	@DeleteMapping(value = "/{filmId}/like/{userId}")
	public ResponseEntity<?> deleteLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
		Film film = filmStorage.deleteLike(filmId, userId);
		if (film == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", "Film with or user not found"));
		}
		return ResponseEntity.ok(film);
	}

	@GetMapping(value = "/popular")
	public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
		try {
			List<Film> popularFilms = filmStorage.getPopularFilms(count);
			return ResponseEntity.ok(popularFilms);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.emptyList());
		}
	}


}
