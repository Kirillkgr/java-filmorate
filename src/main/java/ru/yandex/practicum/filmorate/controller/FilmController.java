package ru.yandex.practicum.filmorate.controller;

import java.time.Duration;
import java.time.LocalDate;
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
import ru.yandex.practicum.filmorate.DTO.FilmDTO;
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
	}

	@GetMapping
	public ResponseEntity<List<Film>> getFilms() {
		if (!filmCollection.isEmpty()) {
			log.info("Получен список фильмов в количестве : {}", filmCollection.size());
			return ResponseEntity.ok(filmCollection.values().stream().toList());
		}

		log.warn("Фильмы отсутствуют");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Film> getFilm(@PathVariable Integer id) {
		if (filmCollection.containsKey(id)) {
			log.info("Получен фильм: {}", filmCollection.get(id).getName());
			return ResponseEntity.ok(filmCollection.get(id));
		}

		log.warn("Не найден фильм : {}", id);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@PutMapping
	public ResponseEntity<Film> updateFilm(@RequestBody Film updateFilm) {
		if (filmCollection.containsKey(updateFilm.getId())) {
			filmCollection.put(updateFilm.getId(), updateFilm);
			log.info("Обновлен фильм: {}", updateFilm.getName());
			return ResponseEntity.ok(updateFilm);
		} else log.warn("Не найден фильм: {}", updateFilm.getName());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(updateFilm);
	}

	@PostMapping
	public ResponseEntity<FilmDTO> createFilm(@Validated @RequestBody FilmDTO newFilm) {
		if (newFilm.getId() == null) {
			newFilm.setId(getNewId());
		} else {
			newFilm.setId(getNewId());
		}

		Film newFilmToSave = Film.builder()
				.id(newFilm.getId())
				.name(newFilm.getName() != null ? newFilm.getName() : "")
				.description(newFilm.getDescription() != null ? newFilm.getDescription() : "")
				.releaseDate(newFilm.getReleaseDate() != null ? newFilm.getReleaseDate() : LocalDate.now())
				.duration(Duration.ofMinutes(newFilm.getDuration() != null ? newFilm.getDuration() : 0))
				.build();

		filmCollection.put(newFilmToSave.getId(), newFilmToSave);
		log.info("Добавлен фильм: {}", newFilm);

		// Возвращаем `duration` в минутах
		FilmDTO response = new FilmDTO(newFilmToSave.getId(),
				newFilmToSave.getName(),
				newFilmToSave.getDescription(),
				newFilmToSave.getReleaseDate(),
				(int) newFilmToSave.getDuration().toMinutes());

		return ResponseEntity.ok(response);
	}

	Integer getNewId() {
		if (id == null) id = 1;
		return id++;
	}
}