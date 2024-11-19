package ru.yandex.practicum.filmorate.storage.user;

import java.util.*;
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
			log.info("Обновлен пользователь с ID: {}", updateUser.getId());
			return updateUser;
		}
		log.warn("Пользователь с ID {} не найден", updateUser.getId());
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
	public boolean deleteFriend(Integer parentId, Integer childId) {
		User parent = usersCollection.get(parentId);
		User child = usersCollection.get(childId);
		if (parent == null || child == null) {
			return false;
		}
		parent.getFriends().remove(childId);
		child.getFriends().remove(parentId);
		log.info("Пользователи {} и {} больше не друзья", parentId, childId);
		return true;
	}

	@Override
	public List<User> getCommonFriends(Integer parentId, Integer childId) {
		User parent = usersCollection.get(parentId);
		User child = usersCollection.get(childId);
		if (parent == null || child == null) {
			return List.of();
		}
		Set<Integer> commonFriends = new HashSet<>(parent.getFriends());
		commonFriends.retainAll(child.getFriends());
		return commonFriends.stream().map(usersCollection::get).toList();
	}

	@Override
	public List<User> getFriends(Integer id) {
		User user = usersCollection.get(id);
		if (user == null) {
			return List.of();
		}
		return user.getFriends().stream()
				.map(usersCollection::get)
				.toList();
	}

	private Integer getNewId() {
		return ++id;
	}
}
