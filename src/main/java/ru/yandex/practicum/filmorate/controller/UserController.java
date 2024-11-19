package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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


	@GetMapping()
	public ResponseEntity<List<User>> getUsers() {
		List<User> users = userStorage.getUsers();
		if (!users.isEmpty()) {
			return ResponseEntity.ok(users);
		} else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	public ResponseEntity<?> getUser(@PathVariable @NotNull @Positive Integer id) {
		User user = userStorage.getUser(id);
		if (user == null) {
			Map<String, String> error = Map.of("error", "User not found for id: " + id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
		}
		return ResponseEntity.ok(user);
	}

	@PutMapping
	public ResponseEntity<User> updateUser(@Validated @RequestBody User updateUser) {
		User actualUser = userStorage.updateUser(updateUser);
		if (actualUser == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(updateUser);
		} else
			return ResponseEntity.ok(actualUser);
	}


	@PostMapping
	public ResponseEntity<User> createUser(@Validated @RequestBody User newUser) {
		User user = userStorage.createUser(newUser);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(newUser);
		} else
			return ResponseEntity.ok(user);
	}

	@PutMapping(value = "/{parentId}/friends/{childId}")
	public ResponseEntity<User> addFriend(@PathVariable @NotNull @Positive Integer parentId, @PathVariable @NotNull @Positive Integer childId) {
		User parent = userStorage.getUser(parentId);
		User child = userStorage.getUser(childId);

		if (parent == null || child == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(parent);
		} else {
			User user = userStorage.addFriend(parentId, childId);
			return ResponseEntity.ok(user);
		}
	}

	@DeleteMapping(value = "/{parentId}/friends/{childId}")
	public ResponseEntity<User> deleteFriend(@PathVariable @NotNull @Positive Integer parentId, @PathVariable @NotNull @Positive Integer childId) {
		User user = userStorage.deleteFriend(parentId, childId);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} else
			return ResponseEntity.ok(user);
	}

	@GetMapping(value = "/{parentId}/friends")
	public ResponseEntity<List<User>> getFriends(@PathVariable @NotNull @Positive Integer parentId) {
		List<User> friends = userStorage.getFriends(parentId);
		if (friends != null) {
			return ResponseEntity.ok(friends);
		} else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@GetMapping(value = "/{parentId}/friends/common/{otherId}")
	public ResponseEntity<List<User>> getCommonFriends(@PathVariable @NotNull @Positive Integer parentId, @PathVariable @NotNull @Positive Integer otherId) {
		List<User> friends = userStorage.getCommonFriends(parentId, otherId);
		if (friends != null) {
			return ResponseEntity.ok(friends);
		} else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
}
