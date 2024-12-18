package ru.yandex.practicum.filmorate.storage.film;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DTO.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmLikeRepository;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InMemoryFilmStorage implements FilmStorage {
    final InMemoryUserStorage inMemoryUserStorage;
    final FilmRepository filmRepository;

    final FilmLikeRepository filmLikeRepository;

    @Override
    public List<Film> getFilms() {
        if (!filmRepository.exists()) {
            List<Film> filmList = filmRepository.getAll();
            log.info("Получен список фильмов в количестве: {}", filmList.size());
            return filmList;
        }
        log.warn("Фильмы отсутствуют");
        return null;
    }

    @Override
    public Film getFilm(Integer id) {
        Film film = filmRepository.get(id);
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
        if (!filmRepository.exists(updateFilm.getId())) {
            log.warn("Не найден фильм для обновления с ID: {}", updateFilm.getId());
            return null;
        }

        Film newFilmToSave = convertFilmDtoToFilm(updateFilm);

        if (filmRepository.exists(newFilmToSave.getId())) {
            newFilmToSave = filmRepository.update(newFilmToSave);
            log.info("Обновлен фильм: {}", updateFilm.getName());

            filmDTO = convertFilmToFilmDto(newFilmToSave);
        }
        return filmDTO;
    }

    @Override
    public Film addLike(Integer id, Integer userId) {
        Film film = filmRepository.get(id);
        User user = inMemoryUserStorage.getUser(userId);
        if (film != null && user != null) {
            filmLikeRepository.save(FilmLike.builder().film(film).user(user).build());
            log.info("Film with id {} was liked by user with id {}", userId, id);
            return film;
        }
        log.info("Film with id {} not found", id);
        return null;
    }

    @Override
    public Film deleteLike(Integer filmId, Integer userId) {
        Film film = filmRepository.get(filmId);
        User user = inMemoryUserStorage.getUser(userId);
        if (film != null && user != null) {
            filmLikeRepository.delete(filmId, userId);
            return film;
        }
        return null;
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        // Получаем список популярных фильмов с их количеством лайков
        List<Map<String, Object>> popularFilms = filmLikeRepository.findPopularFilms(count);

        if (popularFilms.isEmpty()) {
            return List.of();
        }

        // Собираем ID популярных фильмов
        List<Integer> filmIds = popularFilms.stream()
                .map(film -> (Integer) film.get("film_id"))
                .collect(Collectors.toList());

        List<Film> films = filmRepository.getAllById(filmIds);

        // Сортируем фильмы по количеству лайков
        films.sort((f1, f2) -> {
            int f1Likes = popularFilms.stream().filter(film -> film.get("film_id").equals(f1.getId())).findFirst().map(film -> (Integer) film.get("likes")).orElse(0);
            int f2Likes = popularFilms.stream().filter(film -> film.get("film_id").equals(f2.getId())).findFirst().map(film -> (Integer) film.get("likes")).orElse(0);
            return Integer.compare(f2Likes, f1Likes);
        });

        // Ограничиваем результат количеством популярных фильмов
        return films.stream().limit(count).collect(Collectors.toList());
    }

    // Другие методы из репозитория FilmRepository, такие как save, update, get и т.д.


    public static FilmDto convertFilmToFilmDto(Film newFilmToSave) {
        FilmDto filmDTO;
        filmDTO = FilmDto.builder().id(newFilmToSave.getId()).name(newFilmToSave.getName()).description(newFilmToSave.getDescription()).releaseDate(newFilmToSave.getReleaseDate()).duration(newFilmToSave.getDuration()).build();
        return filmDTO;
    }

    private static Film convertFilmDtoToFilm(FilmDto newFilm) {
        return Film.builder().id(newFilm.getId()).name(newFilm.getName()).description(newFilm.getDescription()).releaseDate(newFilm.getReleaseDate()).duration(newFilm.getDuration()).build();
    }

    public Film createFilm(FilmDto newFilm) {
        Film film = convertFilmDtoToFilm(newFilm);
        film=filmRepository.save(film);
        return film;
    }
}
