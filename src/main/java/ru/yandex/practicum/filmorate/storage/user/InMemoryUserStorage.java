package ru.yandex.practicum.filmorate.storage.user;

import java.util.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DTO.FriendDTO;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InMemoryUserStorage implements UserStorage {

	final Map<Integer, User> usersCollection = new HashMap<>();
	int id = 0;

	@Override
	public List<User> getUsers() {
		log.info("Запрос всех пользователей. Количество: {}", usersCollection.size());
		return new ArrayList<>(usersCollection.values());
	}

	@Override
	public User getUser(Integer id) {
		return usersCollection.get(id);
	}

	@Override
	public User updateUser(User updateUser) {
		if (usersCollection.containsKey(updateUser.getId())) {
			usersCollection.put(updateUser.getId(), updateUser);
			log.info("Updated user with ID: {}", updateUser.getId());
			return updateUser;
		}
		log.warn("User with ID {} not found", updateUser.getId());
		return null;
	}

	@Override
	public User createUser(User newUser) {
		if (newUser.getName() == null || newUser.getName().isBlank()) {
			newUser.setName(newUser.getLogin());
		}
		if (usersCollection.containsKey(newUser.getId())) {
			log.warn("Пользователь с ID {} уже существует", newUser.getId());
			return null;
		}
		newUser.setId(getNewId());
		usersCollection.put(newUser.getId(), newUser);
		log.info("Добавлен пользователь с ID: {}", newUser.getId());
		return newUser;
	}

	@Override
	public boolean addFriend(Integer parentId, Integer childId) {
		User parent = usersCollection.get(parentId);
		User child = usersCollection.get(childId);
		if (parent == null || child == null) {
			return false;
		}
		parent.getFriends().add(childId);
		child.getFriends().add(parentId);
		log.info("Пользователи {} и {} стали друзьями", parentId, childId);
		return true;
	}

	@Override
	public void deleteFriend(Integer parentId, Integer childId) {
		User parent = usersCollection.get(parentId);
		User child = usersCollection.get(childId);
		if (parent == null || child == null) {
			return;
		}
		parent.getFriends().remove(childId);
		child.getFriends().remove(parentId);
		log.info("Пользователи {} и {} больше не друзья", parentId, childId);
    }

	@Override
	public List<FriendDTO> getCommonFriends(Integer parentId, Integer childId) {
		return null;
	}

	@Override
	public List<FriendDTO> getFriends(Integer id) {
		return null;
	}

	@Override
	public boolean existsById(Integer parentId) {
		return usersCollection.containsKey(parentId);
	}

	private Integer getNewId() {
		return ++id;
	}
}
