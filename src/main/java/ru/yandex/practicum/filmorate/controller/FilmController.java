package ru.yandex.practicum.filmorate.controller;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

		FilmDto filmDto = filmStorage.updateFilm(updateFilm);

		if (filmDto == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		return ResponseEntity.ok(filmDto);
	}

	@PostMapping
	public ResponseEntity<FilmDto> createFilm(@Validated @RequestBody FilmDto newFilm) {
		newFilm = filmStorage.createFilm(newFilm);
		return ResponseEntity.ok(newFilm);
	}
}
