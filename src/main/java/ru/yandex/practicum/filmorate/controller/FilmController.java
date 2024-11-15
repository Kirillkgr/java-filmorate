package ru.yandex.practicum.filmorate.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
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

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {
	final Map<Integer, Film> filmCollection;
	Integer id;

	public FilmController() {
		filmCollection = new HashMap<>();
		id = 1;
	}

	@GetMapping
	public ResponseEntity<List<Film>> getFilms() {
		if (!filmCollection.isEmpty()) {
			log.info("Получен список фильмов в количестве: {}", filmCollection.size());
			return ResponseEntity.ok(new ArrayList<>(filmCollection.values()));
		}

		log.warn("Фильмы отсутствуют");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Film> getFilm(@PathVariable Integer id) {
		Film film = filmCollection.get(id);
		if (film != null) {
			log.info("Получен фильм: {}", film.getName());
			return ResponseEntity.ok(film);
		}

		log.warn("Не найден фильм с ID: {}", id);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@PutMapping
	public ResponseEntity<FilmDto> updateFilm(@Validated @RequestBody FilmDto updateFilm) {
		if (!filmCollection.containsKey(updateFilm.getId())) {
			log.warn("Не найден фильм для обновления с ID: {}", updateFilm.getId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(updateFilm);
		}

		Film newFilmToSave = Film.builder()
				.id(updateFilm.getId())
				.name(updateFilm.getName())
				.description(updateFilm.getDescription())
				.releaseDate(updateFilm.getReleaseDate())
				.duration(Duration.ofMinutes(updateFilm.getDuration()))
				.build();

		if (filmCollection.containsKey(newFilmToSave.getId())) {
			filmCollection.put(updateFilm.getId(), newFilmToSave);
			log.info("Обновлен фильм: {}", updateFilm.getName());

			FilmDto filmDTO = FilmDto.builder()
					.id(newFilmToSave.getId())
					.name(newFilmToSave.getName())
					.description(newFilmToSave.getDescription())
					.releaseDate(newFilmToSave.getReleaseDate())
					.duration((int) newFilmToSave.getDuration().toMinutes())
					.build();
			return ResponseEntity.ok(filmDTO);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	@PostMapping
	public ResponseEntity<?> createFilm(@Validated @RequestBody FilmDto newFilm) {
		LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);

		if (newFilm.getReleaseDate().isBefore(minReleaseDate)) {
			log.warn("Некорректная дата релиза фильма: {}", newFilm.getReleaseDate());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(newFilm);
		}

		newFilm.setId(newFilm.getId() == null ? getNewId() : newFilm.getId());

		Film newFilmToSave = Film
				.builder()
				.id(newFilm.getId())
				.name(newFilm.getName())
				.description(newFilm.getDescription())
				.releaseDate(newFilm.getReleaseDate())
				.duration(Duration.ofMinutes(newFilm.getDuration()))
				.build();

		filmCollection.put(newFilmToSave.getId(), newFilmToSave);
		log.info("Добавлен фильм: {}", newFilmToSave);

		FilmDto response = new FilmDto(newFilmToSave.getId(), newFilmToSave.getName(), newFilmToSave.getDescription(), newFilmToSave.getReleaseDate(), (int) newFilmToSave.getDuration().toMinutes());

		return ResponseEntity.ok(response);
	}


	private Integer getNewId() {
		return id++;
	}
}
