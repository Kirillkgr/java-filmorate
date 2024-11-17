package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
	List<User> getUsers();

	User getUser(Integer id);

	User updateUser(User updateUser);

	User createUser(User newUser);
}
