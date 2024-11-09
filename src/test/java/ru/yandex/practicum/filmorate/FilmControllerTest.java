package ru.yandex.practicum.filmorate;

import java.util.Objects;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.controller.FilmController;

import java.util.List;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
	private FilmController filmController;
	private Film testFilm;

	@BeforeEach
	public void setUp() {
		filmController = new FilmController();
		testFilm = Film.builder()
				.name("Test Film")
				.description("Test Description")
				.releaseDate(LocalDate.of(2000, 1, 1))
				.duration(Duration.ofMinutes(120))
				.build();
	}

	@Test
	public void testCreateFilm() {
		ResponseEntity<Boolean> response = filmController.createFilm(testFilm);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Boolean.TRUE, response.getBody());
		assertEquals(testFilm, filmController.getFilm(testFilm.getId()).getBody());
	}

	@Test
	public void testCreateFilmWithExistingId() {
		filmController.createFilm(testFilm);
		ResponseEntity<Boolean> response = filmController.createFilm(testFilm);
		assertEquals(Boolean.TRUE, response.getBody());
	}

	@Test
	public void testGetAllFilms() {
		filmController.createFilm(testFilm);
		ResponseEntity<List<Film>> response = filmController.getFilms();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(1, response.getBody().size());
		assertTrue(response.getBody().contains(testFilm));
	}

	@Test
	public void testGetAllFilmsWhenEmpty() {
		ResponseEntity<List<Film>> response = filmController.getFilms();
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testGetFilm() {
		filmController.createFilm(testFilm);
		ResponseEntity<Film> response = filmController.getFilm(testFilm.getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(testFilm, response.getBody());
	}

	@Test
	public void testGetFilmNotFound() {
		ResponseEntity<Film> response = filmController.getFilm(999);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testUpdateFilm() {
		filmController.createFilm(testFilm);
		testFilm.setDescription("Updated Description");
		ResponseEntity<Boolean> response = filmController.updateFilm(testFilm);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Boolean.TRUE, response.getBody());
		assertEquals("Updated Description", Objects.requireNonNull(filmController.getFilm(testFilm.getId()).getBody()).getDescription());
	}

	@Test
	public void testUpdateFilmNotFound() {
		Film newFilm = Film.builder()
				.id(2)
				.name("Nonexistent Film")
				.description("Description")
				.releaseDate(LocalDate.of(2010, 5, 5))
				.duration(Duration.ofMinutes(120))
				.build();
		ResponseEntity<Boolean> response = filmController.updateFilm(newFilm);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertNotEquals(Boolean.TRUE, response.getBody());
	}
}
