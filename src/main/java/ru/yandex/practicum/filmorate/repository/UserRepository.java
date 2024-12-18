package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserRepository {

    User save(User user);

    Boolean exist(int id);

    User update(User user);

    User get(Integer id);

    void delete(Integer id);

    List<User> getAll();

    List<User> findAllById(Set<Integer> userIds);
}
