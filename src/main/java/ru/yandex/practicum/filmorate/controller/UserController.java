package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	final UserStorage userStorage;

	@GetMapping
	public ResponseEntity<List<User>> getUsers() {
		List<User> users = userStorage.getUsers();
		return users.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(@PathVariable @NotNull @Positive Integer id) {
		User user = userStorage.getUser(id);
		return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PutMapping
	public ResponseEntity<User> updateUser(@Validated @RequestBody User updateUser) {
		User actualUser = userStorage.updateUser(updateUser);
		if (actualUser == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Или передать JSON с описанием ошибки.
		}
		return ResponseEntity.ok(actualUser);
	}

	@PostMapping
	public ResponseEntity<User> createUser(@Validated @RequestBody User newUser) {
		User user = userStorage.createUser(newUser);
		return user != null ? ResponseEntity.status(HttpStatus.CREATED).body(user) : ResponseEntity.status(HttpStatus.CONFLICT).build();
	}

	@PutMapping("/{parentId}/friends/{childId}")
	public ResponseEntity<Void> addFriend(@PathVariable @NotNull @Positive Integer parentId, @PathVariable @NotNull @Positive Integer childId) {
		if (!userStorage.existsById(parentId) || !userStorage.existsById(childId)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		boolean success = userStorage.addFriend(parentId, childId);
		return success ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@DeleteMapping("/{parentId}/friends/{childId}")
	public ResponseEntity<?> deleteFriend(@PathVariable @NotNull @Positive Integer parentId, @PathVariable @NotNull @Positive Integer childId) {
		if (!userStorage.existsById(parentId) || !userStorage.existsById(childId)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Один из пользователей не найден."); // JSON или строка.
		}
		boolean success = userStorage.deleteFriend(parentId, childId);
		return success ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}


	@GetMapping("/{parentId}/friends")
	public ResponseEntity<List<User>> getFriends(@PathVariable @NotNull @Positive Integer parentId) {
		List<User> friends = userStorage.getFriends(parentId);
		return ResponseEntity.ok(friends);
	}

	@GetMapping("/{parentId}/friends/common/{otherId}")
	public ResponseEntity<List<User>> getCommonFriends(@PathVariable @NotNull @Positive Integer parentId, @PathVariable @NotNull @Positive Integer otherId) {
		List<User> friends = userStorage.getCommonFriends(parentId, otherId);
		return ResponseEntity.ok(friends);
	}
}
