package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.DTO.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

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
		// Проверка даты выпуска
		if (updateFilm.getReleaseDate().isAfter(LocalDate.now())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FilmDto("Invalid release date"));
		}

		FilmDto filmDto = filmStorage.updateFilm(updateFilm);

		if (filmDto == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(filmDto);
	}

	@PostMapping
	public ResponseEntity<FilmDto> createFilm(@Validated @RequestBody FilmDto newFilm) {
		// Проверка даты выпуска
		if (newFilm.getReleaseDate().isAfter(LocalDate.now())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new FilmDto("Invalid release date"));
		}

		newFilm = filmStorage.createFilm(newFilm);
		return ResponseEntity.ok(newFilm);
	}

	// Новый метод для добавления лайка
	@PutMapping(value = "/{filmId}/like/{userId}")
	public ResponseEntity<Film> addLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
		Film film = filmStorage.addLike(filmId, userId);
		if (film == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(film);
	}

	@DeleteMapping(value = "/{filmId}/like/{userId}")
	public ResponseEntity<Film> deleteLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
		Film film = filmStorage.deleteLike(filmId, userId);
		if (film == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(film);
	}

	@GetMapping(value = "/popular?count={count}")
	public ResponseEntity<List<Film>> getPopularFilms(@PathVariable Integer count) {
		List<Film> filmCollection = filmStorage.getPopularFilms(count);
		if (filmCollection != null && !filmCollection.isEmpty()) {
			return ResponseEntity.ok(new ArrayList<>(filmCollection));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
}
