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
		if (users != null) {
			return ResponseEntity.ok(users);
		} else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<User> getUser(@PathVariable @NotNull @Positive Integer id) {
		User user = userStorage.getUser(id);
		if (user != null) {
			return ResponseEntity.ok(user);
		} else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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


}