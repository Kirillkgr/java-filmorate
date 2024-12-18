package ru.yandex.practicum.filmorate.storage.user;

import java.util.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmLikeRepository;
import ru.yandex.practicum.filmorate.repository.FriendshipRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Slf4j
@Component
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InMemoryUserStorage implements UserStorage {
    final UserRepository userRepository;
    final FriendshipRepository friendshipRepository;

    @Override
    public List<User> getUsers() {
        List<User> users = userRepository.getAll();
        if (users == null) {
            log.warn("Пользователи отсутствуют");
            return new ArrayList<>();
        }
        log.info("Запрос всех пользователей. Количество: {}", users.size());
        return users;
    }

    @Override
    public User getUser(Integer id) {
        User user = userRepository.get(id);
        if (user == null) {
            log.warn("Пользователь с ID {} не найден", id);
            return null;
        }
        return user;
    }

    @Override
    public User updateUser(User updateUser) {
        User user = userRepository.update(updateUser);
        if (user == null) {
            log.warn("User with ID {} not found", updateUser.getId());
            return null;
        }
        log.info("Updated user with ID: {}", updateUser.getId());
        return user;
    }

    @Override
    public User createUser(User newUser) {
        User user = userRepository.save(newUser);
        log.info("Добавлен пользователь с ID: {}", newUser.getId());
        return user;
    }

    @Override
    public boolean addFriend(Integer parentId, Integer childId) {
        User parent = userRepository.get(parentId);
        User child = userRepository.get(childId);
        if (parent == null || child == null) {
            return false;
        }
        friendshipRepository.addFriend(parent, child);
        log.info("Пользователи {} и {} стали друзьями", parentId, childId);
        return true;
    }

    @Override
    public boolean deleteFriend(Integer parentId, Integer childId) {
        User parent = userRepository.get(parentId);
        User child = userRepository.get(childId);
        if (parent == null || child == null) {
            return false;
        }
        friendshipRepository.removeFriend(parent, child);
        log.info("Пользователи {} и {} больше не друзья", parentId, childId);
        return true;
    }

    @Override
    public List<User> getCommonFriends(Integer parentId, Integer childId) {
        User parent = userRepository.get(parentId);
        User child = userRepository.get(childId);

        if (parent == null || child == null) {
            return List.of();
        }

        Set<Integer> commonFriendsParentId = new HashSet<>(friendshipRepository.getFriends(parentId));
        Set<Integer> commonFriendsChildId = new HashSet<>(friendshipRepository.getFriends(childId));
        // Пересечение двух множеств
        commonFriendsParentId.retainAll(commonFriendsChildId);
        // Возвращаем список общих друзей
        return userRepository.findAllById(commonFriendsParentId);
    }


    @Override
    public List<User> getFriends(Integer id) {
        User user = userRepository.get(id);
        if (user == null) {
            log.warn("Пользователь с ID {} не найден", id);
            return List.of();
        }
        Set<Integer> userActualFriends = friendshipRepository.getFriends(id);
        log.info("Пользователь {} имеет {} друзей", id, userActualFriends.size());
        return userRepository.findAllById(userActualFriends);
    }

    @Override
    public boolean existsById(Integer parentId) {
        return userRepository.exist(parentId);
    }
}
