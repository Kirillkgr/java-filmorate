package ru.yandex.practicum.filmorate;

import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import ru.yandex.practicum.filmorate.controller.UserController;

public class UserControllerTest {
	private UserController userController;
	private User testUser;

	@BeforeEach
	public void setUp() {
		userController = new UserController();
		testUser = User.builder().id(1).name("Test User").email("test@example.com").build();
	}

	@Test
	public void testCreateUser() {
		ResponseEntity<User> response = userController.createUser(testUser);
		User user = response.getBody();
		assertNotEquals(null, response.getBody());
		assertNotEquals(null, user);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(user.getId());
		assertEquals(user.getName(), testUser.getName());
	}

	@Test
	public void testGetAllUsers() {
		userController.createUser(testUser);
		ResponseEntity<List<User>> response = userController.getUsers();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(1, Objects.requireNonNull(response.getBody()).size());
		assertTrue(response.getBody().contains(testUser));
	}

	@Test
	public void testGetAllUsersWhenEmpty() {
		ResponseEntity<List<User>> response = userController.getUsers();

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testGetUser() {
		userController.createUser(testUser);
		ResponseEntity<User> response = userController.getUser(testUser.getId());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(testUser, response.getBody());
	}

	@Test
	public void testGetUserNotFound() {
		ResponseEntity<User> response = userController.getUser(999);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testUpdateUser() {
		userController.createUser(testUser);
		testUser.setEmail("updated@example.com");
		ResponseEntity<Boolean> response = userController.updateUser(testUser);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Boolean.TRUE, response.getBody());
		assertEquals("updated@example.com", Objects.requireNonNull(userController.getUser(testUser.getId()).getBody()).getEmail());
	}

	@Test
	public void testUpdateUserNotFound() {
		User newUser = User.builder().id(2).name("Nonexistent User").build();

		ResponseEntity<Boolean> response = userController.updateUser(newUser);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertNotEquals(Boolean.TRUE, response.getBody());
	}
}