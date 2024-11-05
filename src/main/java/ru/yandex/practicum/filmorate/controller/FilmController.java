package ru.yandex.practicum.filmorate.controller;

import java.util.HashMap;
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
import ru.yandex.practicum.filmorate.model.Film;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {
	final Map<Integer, Film> filmCollection;
	Integer id;
	FilmController() {
		filmCollection = new HashMap<>();
		id = 0;
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<Film> getFilms(@PathVariable Integer id) {
		if (filmCollection.containsKey(id)) {
			log.info("Получен фильм: {}", filmCollection.get(id).getName());
			return ResponseEntity.ok(filmCollection.get(id));
		}
		log.info("Не найден фильм : {}", id);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	@PutMapping
	public ResponseEntity<Boolean> updateFilm(@RequestBody Film updateFilm) {
		if (filmCollection.containsKey(updateFilm.getId())) {
			filmCollection.put(updateFilm.getId(), updateFilm);
			log.info("Обновлен фильм: {}", updateFilm.getName());
			return ResponseEntity.ok(true);
		} else
			log.info("Не найден фильм: {}", updateFilm.getName());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(false);
	}
	@PostMapping
	public ResponseEntity<Boolean> createFilm(@Validated @RequestBody Film newFilm) {
		if (filmCollection.containsKey(newFilm.getId()) || newFilm.getId() == 0) {
			log.info("Фильм с id {} уже существует  или  id = 0 : {}", newFilm.getId(), newFilm.getName());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(false);
		}
		newFilm.setId(getNewId());
		filmCollection.put(newFilm.getId(), newFilm);
		log.info("Добавлен фильм: {}", newFilm);
		return ResponseEntity.ok(true);
	}
	Integer getNewId() {
		return id++;
	}
}
