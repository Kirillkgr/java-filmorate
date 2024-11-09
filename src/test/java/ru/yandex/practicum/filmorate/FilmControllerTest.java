package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

	private FilmController filmController;

	@BeforeEach
	void setUp() {
		filmController = new FilmController();
	}

	@Test
	void getFilms_noFilms_returnsNotFound() {
		ResponseEntity<List<Film>> response = filmController.getFilms();
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	void getFilms_withFilms_returnsFilms() {
		Film testFilm = Film.builder().id(1).name("Test Film").build();
		filmController.createFilm(testFilm);

		ResponseEntity<List<Film>> response = filmController.getFilms();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(1, response.getBody().size());
		assertEquals(testFilm.getName(), response.getBody().get(0).getName());
	}

	@Test
	void getFilm_existingFilm_returnsFilm() {
		Film testFilm = Film.builder().name("Test Film").build();
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
		Film testFilm = Film.builder().id(1).name("Test Film").build();
		filmController.createFilm(testFilm);

		Film updatedFilm = Film.builder().id(1).name("Updated Film").build();
		ResponseEntity<Film> response = filmController.updateFilm(updatedFilm);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Updated Film", response.getBody().getName());

		ResponseEntity<Film> getFilmResponse = filmController.getFilm(1);
		assertEquals("Updated Film", getFilmResponse.getBody().getName());
	}

	@Test
	void updateFilm_nonExistingFilm_returnsNoContent() {
		Film nonExistingFilm = Film.builder().id(2).name("Non-Existing Film").build();
		ResponseEntity<Film> response = filmController.updateFilm(nonExistingFilm);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(nonExistingFilm.getName(), response.getBody().getName());
	}

	@Test
	void createFilm_newFilm_createsFilm() {
		Film newFilm = Film.builder().name("New Film").build();
		ResponseEntity<Film> response = filmController.createFilm(newFilm);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("New Film", response.getBody().getName());
	}
}
