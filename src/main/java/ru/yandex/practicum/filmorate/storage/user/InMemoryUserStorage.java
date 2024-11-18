package ru.yandex.practicum.filmorate.storage.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InMemoryUserStorage implements UserStorage {

	final Map<Integer, User> usersCollection;
	Integer id;

	public InMemoryUserStorage() {
		usersCollection = new HashMap<>();
		id = 0;
	}

	@Override
	public List<User> getUsers() {
		if (!usersCollection.isEmpty()) {
			log.info("Выдан список пользователей в количестве : {}", usersCollection.size());
			return usersCollection.values().stream().toList();
		}
		log.info("В базе отсутствуют фильмы");
		return null;
	}

	@Override
	public User getUser(Integer id) {
		if (usersCollection.containsKey(id)) {
			log.info("Получен пользователь : {}", usersCollection.get(id).getName());
			return usersCollection.get(id);
		}
		log.info("Не найден пользователь : {}", id);
		return null;
	}

	@Override
	public User updateUser(User updateUser) {
		if (usersCollection.containsKey(updateUser.getId())) {
			usersCollection.put(updateUser.getId(), updateUser);
			log.info("Обновлен пользователь: {}", updateUser.getName());
			return updateUser;
		} else {
			log.warn("Не найден пользователь с id: {}", updateUser.getId());
			return null;
		}
	}

	@Override
	public User createUser(User newUser) {

		if (newUser.getName() == null || newUser.getName().isBlank()) {
			newUser.setName(newUser.getLogin());
		}

		User userExist = newUser.getId() != null ? usersCollection.get(newUser.getId()) : null;
		if (userExist != null) {
			log.warn("Пользователь с id: {} уже существует", newUser.getId());
			return null;
		}

		newUser.setId(getNewId());
		usersCollection.put(newUser.getId(), newUser);
		log.info("Добавлен пользователь: {}", newUser);
		return newUser;
	}

	@Override
	public User addFriend(Integer id, Integer friendId) {
		return null;
	}

	private Integer getNewId() {
		if (id == null) {
			id = 0;
		}
		return ++id;
	}
}
