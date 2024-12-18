package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface FriendshipRepository {
    void addFriend(User parent, User child);
    void removeFriend(User parent, User child);
    Friendship getAll(User parent);
    Boolean isFriendshipConfirmed(User parent, User child);
    Boolean checkFriendship(User parent, User child);

    Set<Integer> getFriends(Integer userId);
}
