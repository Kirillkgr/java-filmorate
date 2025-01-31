package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


class UserControllerTest {

	private UserController userController;

	@BeforeEach
	void setUp() {
		UserStorage userStorage = new InMemoryUserStorage();
		userController = new UserController(userStorage);
	}

	@Test
	void getUsers_noUsers_returnsNotFound() {
		ResponseEntity<List<User>> response = userController.getUsers();
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	void getUsers_withUsers_returnsUsers() {
		User testUser = User.builder().id(1).name("Test User").email("test@example.com").build();
		userController.createUser(testUser);

		ResponseEntity<List<User>> response = userController.getUsers();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(1, response.getBody().size());
		assertEquals(testUser.getName(), response.getBody().getFirst().getName());
	}

	@Test
	void getUser_existingUser_returnsUser() {
		User testUser = User.builder().id(1).name("Test User").email("test@example.com").build();
		userController.createUser(testUser);

		ResponseEntity<User> response = userController.getUser(1);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(testUser.getName(), response.getBody().getName());
	}

	@Test
	void getUser_nonExistingUser_returnsNotFound() {
		ResponseEntity<User> response = userController.getUser(1);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void updateUser_existingUser_updatesUser() {
		User testUser = User.builder().id(1).name("Test User").email("test@example.com").build();
		userController.createUser(testUser);

		User updatedUser = User.builder().id(1).name("Updated User").email("updated@example.com").build();
		ResponseEntity<User> response = (ResponseEntity<User>) userController.updateUser(updatedUser);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Updated User", response.getBody().getName());

		ResponseEntity<User> getUserResponse = userController.getUser(1);
		assertEquals("Updated User", Objects.requireNonNull(getUserResponse.getBody()).getName());
	}

	@Test
	void updateUser_nonExistingUser_returnsNoContent() {
		User nonExistingUser = User.builder().id(2).name("Non-Existing User").email("nonexistent@example.com").build();
		ResponseEntity<User> response = (ResponseEntity<User>)userController.updateUser(nonExistingUser);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void createUser_newUser_createsUser() {
		User newUser = User.builder().login("login").name("New User").email("new@example.com").build();
		ResponseEntity<User> response = userController.createUser(newUser);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("New User", response.getBody().getName());
	}

	@Test
	void createUser_duplicateId_conflict() {
		User firstUser = User.builder().id(1).login("login").name("First User").email("first@example.com").build();
		userController.createUser(firstUser);

		User duplicateUser = User.builder().id(1).name("First User").email("duplicate@example.com").build();
		ResponseEntity<User> response = userController.createUser(duplicateUser);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}
}
