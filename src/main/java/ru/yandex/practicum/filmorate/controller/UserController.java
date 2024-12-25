package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.DTO.FriendDTO;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    @Qualifier(value = "userDbStorage")
    final UserStorage userStorage;

    public UserController(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userStorage.getUsers();
        return users.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable @NotNull @Positive Integer id) {
        User user = userStorage.getUser(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Validated @RequestBody User updateUser) {
        User actualUser = userStorage.updateUser(updateUser);
        if (actualUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User with ID " + updateUser.getId() + " not found"));
        }
        return ResponseEntity.ok(actualUser);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Validated @RequestBody User newUser) {
        User user = userStorage.createUser(newUser);
        return user != null ? ResponseEntity.status(HttpStatus.CREATED).body(user) : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PutMapping("/{parentId}/friends/{childId}")
    public ResponseEntity<?> addFriend(@PathVariable @NotNull @Positive Integer parentId, @PathVariable @NotNull @Positive Integer childId) {
        if (!userStorage.existsById(parentId) || !userStorage.existsById(childId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User(s) not found: parentId=" + parentId + ", childId=" + childId));
        }
        boolean success = userStorage.addFriend(parentId, childId);
        if (success) {
            List<FriendDTO> friends = userStorage.getFriends(parentId);
            return ResponseEntity.ok(friends);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/{parentId}/friends/{childId}")
    public ResponseEntity<?> deleteFriend(@PathVariable @NotNull @Positive Integer parentId, @PathVariable @NotNull @Positive Integer childId) {
        if (!userStorage.existsById(parentId) || !userStorage.existsById(childId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User(s) not found: parentId=" + parentId + ", childId=" + childId));
        }
        userStorage.deleteFriend(parentId, childId);
        return ResponseEntity.noContent().build();

    }


    @GetMapping("/{parentId}/friends")
    public ResponseEntity<?> getFriends(@PathVariable @NotNull @Positive Integer parentId) {
        if (!userStorage.existsById(parentId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User with ID " + parentId + " not found"));
        }
        List<FriendDTO> friends = userStorage.getFriends(parentId);
        List<User> friendsUser = friends.stream()
                .map(friend -> userStorage.getUser(friend.getId()))
                .collect(Collectors.toList());


        return ResponseEntity.ok(friendsUser);
    }

    @GetMapping("/{parentId}/friends/common/{otherId}")
    public ResponseEntity<List<FriendDTO>> getCommonFriends(@PathVariable @NotNull @Positive Integer parentId, @PathVariable @NotNull @Positive Integer otherId) {
        List<FriendDTO> friends = userStorage.getCommonFriends(parentId, otherId);
        return ResponseEntity.ok(friends);
    }

}
