package ru.yandex.practicum.filmorate;

import java.time.LocalDate;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.DTO.FilmDto;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

	private FilmController filmController;

	@BeforeEach
	void setUp() {
		FilmStorage filmStorage = new InMemoryFilmStorage(new InMemoryUserStorage());
		filmController = new FilmController(filmStorage);
	}

	@Test
	void getFilms_noFilms_returnsNotFound() {
		ResponseEntity<List<Film>> response = filmController.getFilms();
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	void getFilms_withFilms_returnsFilms() {
		FilmDto testFilm = FilmDto.builder().id(1).name("Test Film").duration(12).releaseDate(LocalDate.now()).build();
		filmController.createFilm(testFilm);

		ResponseEntity<List<Film>> response = filmController.getFilms();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(1, response.getBody().size());
		assertEquals(testFilm.getName(), response.getBody().getFirst().getName());
	}

	@Test
	void getFilm_existingFilm_returnsFilm() {
		FilmDto testFilm = FilmDto.builder().id(1).name("Test Film").description("test description").duration(10).releaseDate(LocalDate.now()).build();


		filmController.createFilm(testFilm);

		ResponseEntity<Film> response = filmController.getFilm(1);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(testFilm.getName(), response.getBody().getName());
	}

	@Test
	void getFilm_nonExistingFilm_returnsNotFound() {
		ResponseEntity<Film> response = filmController.getFilm(1);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	void updateFilm_existingFilm_updatesFilm() {
		FilmDto testFilm = FilmDto.builder().id(1).name("Updated Film").description("for test").duration(10).releaseDate(LocalDate.now()).build();
		filmController.createFilm(testFilm);

		ResponseEntity<FilmDto> response = filmController.updateFilm(testFilm);
		assertEquals(HttpStatus.OK, response.getStatusCode());

		ResponseEntity<Film> getFilmResponse = filmController.getFilm(1);
		assertEquals("Updated Film", Objects.requireNonNull(getFilmResponse.getBody()).getName());
	}

	@Test
	void updateFilm_nonExistingFilm_returnsNoContent() {

		FilmDto nonExistingFilm = FilmDto.builder().id(2).name("Non-Existing Film").releaseDate(LocalDate.now()).build();
		ResponseEntity<FilmDto> response = filmController.updateFilm(nonExistingFilm);


		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void createFilm_newFilm_createsFilm() {
		FilmDto newFilm = FilmDto.builder().name("New Film").duration(30).releaseDate(LocalDate.now()).build();
		ResponseEntity<FilmDto> response = filmController.createFilm(newFilm);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("New Film", response.getBody().getName());
	}
}
