package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

	private UserController userController;

	@BeforeEach
	void setUp() {
		userController = new UserController();
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
		assertEquals(testUser.getName(), response.getBody().get(0).getName());
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
		assertNull(response.getBody());
	}

	@Test
	void updateUser_existingUser_updatesUser() {
		User testUser = User.builder().id(1).name("Test User").email("test@example.com").build();
		userController.createUser(testUser);

		User updatedUser = User.builder().id(1).name("Updated User").email("updated@example.com").build();
		ResponseEntity<User> response = userController.updateUser(updatedUser);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Updated User", response.getBody().getName());

		ResponseEntity<User> getUserResponse = userController.getUser(1);
		assertEquals("Updated User", getUserResponse.getBody().getName());
	}

	@Test
	void updateUser_nonExistingUser_returnsNoContent() {
		User nonExistingUser = User.builder().id(2).name("Non-Existing User").email("nonexistent@example.com").build();
		ResponseEntity<User> response = userController.updateUser(nonExistingUser);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(nonExistingUser.getName(), response.getBody().getName());
	}

	@Test
	void createUser_newUser_createsUser() {
		User newUser = User.builder().name("New User").email("new@example.com").build();
		ResponseEntity<User> response = userController.createUser(newUser);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("New User", response.getBody().getName());
	}

	@Test
	void createUser_duplicateId_conflict() {
		User firstUser = User.builder().id(1).name("First User").email("first@example.com").build();
		userController.createUser(firstUser);

		User duplicateUser = User.builder().id(1).name("First User").email("duplicate@example.com").build();
		ResponseEntity<User> response = userController.createUser(duplicateUser);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertEquals(firstUser.getName(), response.getBody().getName());
	}
}
