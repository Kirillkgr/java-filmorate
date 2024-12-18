package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
	List<User> getUsers();

	User getUser(Integer id);

	User updateUser(User updateUser);

	User createUser(User newUser);

	boolean addFriend(@NotNull @Positive Integer id, @NotNull @Positive Integer friendId);

	boolean deleteFriend(@NotNull @Positive Integer id, @NotNull @Positive Integer friendId);

	List<User> getCommonFriends(@NotNull @Positive Integer id, @NotNull @Positive Integer otherId);

	List<User> getFriends(@NotNull @Positive Integer id);

	boolean existsById(@NotNull @Positive Integer parentId);
}
