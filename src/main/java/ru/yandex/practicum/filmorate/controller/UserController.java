package ru.yandex.practicum.filmorate.controller;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import lombok.AccessLevel;
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

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
	final Map<Integer, User> usersCollection;
	Integer id;

	public UserController() {
		usersCollection = new HashMap<>();
	}

	@GetMapping()
	public ResponseEntity<List<User>> getUsers() {
		if (!usersCollection.isEmpty()) {
			log.info("Выдан список пользователей в количестве : {}", usersCollection.size());
			return ResponseEntity.ok(usersCollection.values().stream().toList());
		}
		log.info("В базе отсутствуют фильмы");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<User> getUser(@Validated @PathVariable Integer id) {
		if (usersCollection.containsKey(id)) {
			log.info("Получен пользователь : {}", usersCollection.get(id).getName());
			return ResponseEntity.ok(usersCollection.get(id));
		}
		log.info("Не найден пользователь : {}", id);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@PutMapping
	public ResponseEntity<User> updateUser(@Validated @RequestBody User updateUser) {
		if (usersCollection.containsKey(updateUser.getId())) {
			usersCollection.put(updateUser.getId(), updateUser);
			log.info("Обновлен пользователь: {}", updateUser.getName());
			return ResponseEntity.ok(updateUser);
		} else {
			log.warn("Не найден пользователь с id: {}", updateUser.getId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(updateUser);
		}
	}


	@PostMapping
	public ResponseEntity<User> createUser(@Validated @RequestBody User newUser) {
		// Устанавливаем имя пользователя по умолчанию, если оно не указано
		if (newUser.getName() == null || newUser.getName().isBlank()) {
			newUser.setName(newUser.getLogin());
		}

		// Проверяем, существует ли пользователь с таким ID
		User userExist = newUser.getId() != null ? usersCollection.get(newUser.getId()) : null;
		if (userExist != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(newUser);
		}

		// Назначаем ID, если он не задан, и добавляем пользователя в коллекцию
		newUser.setId(getNewId());
		usersCollection.put(newUser.getId(), newUser);
		log.info("Добавлен пользователь: {}", newUser);
		return ResponseEntity.ok(newUser);
	}

	private Integer getNewId() {
		if (id == null) {
			id = 0;
		}
		return ++id;
	}

}