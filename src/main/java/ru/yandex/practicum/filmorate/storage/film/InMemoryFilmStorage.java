package ru.yandex.practicum.filmorate.storage.film;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DTO.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InMemoryFilmStorage implements FilmStorage {

	final Map<Integer, Film> filmCollection;
	Integer id;

	public InMemoryFilmStorage() {
		filmCollection = new HashMap<>();
		id = 0;
	}


	@Override
	public List<Film> getFilms() {
		if (!filmCollection.isEmpty()) {
			log.info("Получен список фильмов в количестве: {}", filmCollection.size());
			return new ArrayList<>(filmCollection.values());
		}
		log.warn("Фильмы отсутствуют");
		return null;
	}

	@Override
	public Film getFilm(Integer id) {
		Film film = filmCollection.get(id);
		if (film != null) {
			log.info("Получен фильм: {}", film.getName());
			return film;
		}
		log.warn("Не найден фильм с ID: {}", id);
		return null;
	}

	@Override
	public FilmDto updateFilm(FilmDto updateFilm) {
		FilmDto filmDTO = null;
		if (!filmCollection.containsKey(updateFilm.getId())) {
			log.warn("Не найден фильм для обновления с ID: {}", updateFilm.getId());
			return null;
		}

		Film newFilmToSave = convertFilmDtoToFilm(updateFilm);

		if (filmCollection.containsKey(newFilmToSave.getId())) {
			filmCollection.put(updateFilm.getId(), newFilmToSave);
			log.info("Обновлен фильм: {}", updateFilm.getName());

			filmDTO = convertFilmToFilmDto(newFilmToSave);
		}
		return filmDTO;
	}

	@Override
	public Film addLike(Integer id, Integer userId) {
		Film film = filmCollection.get(id);
		if (film != null) {
			film.getLikeUsers().add(userId);
			log.info("Added like from user {} to film {}", userId, id);
			return film;
		}
		log.info("Film with id {} not found", id);
		return null;
	}

	@Override
	public Film deleteLike(Integer filmId, Integer userId) {
		Film film = filmCollection.get(filmId);
		if (film != null) {
			film.getLikeUsers().remove(userId);
			return film;
		}
		return null;
	}

	@Override
	public List<Film> getPopularFilms(Integer count) {
		if (!filmCollection.isEmpty()) {
			return filmCollection.values().stream().sorted((f1, f2) -> Integer.compare(f2.getLikesIds().size(), f1.getLikesIds().size())).limit(count).collect(Collectors.toList());
		}
		return List.of();
	}

	public static FilmDto convertFilmToFilmDto(Film newFilmToSave) {
		FilmDto filmDTO;
		filmDTO = FilmDto.builder().id(newFilmToSave.getId()).name(newFilmToSave.getName()).description(newFilmToSave.getDescription()).releaseDate(newFilmToSave.getReleaseDate()).duration((int) newFilmToSave.getDuration().toMinutes()).build();
		return filmDTO;
	}

	private static Film convertFilmDtoToFilm(FilmDto newFilm) {
		return Film.builder().id(newFilm.getId()).name(newFilm.getName()).description(newFilm.getDescription()).releaseDate(newFilm.getReleaseDate()).duration(Duration.ofMinutes(newFilm.getDuration())).build();
	}

	public Film createFilm(FilmDto newFilm) {
		int newId = getNextId(); // Method that returns the next available ID
		Film film = new Film(newId, newFilm.getName(), newFilm.getDescription(), newFilm.getReleaseDate(), Duration.ofMinutes(newFilm.getDuration()), new HashSet<>());
		filmCollection.put(newId, film);
		return film;
	}

	private int getNextId() {
		return filmCollection.size() + 1;
	}

}
