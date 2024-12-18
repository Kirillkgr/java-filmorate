package ru.yandex.practicum.filmorate.repository.Impl;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public class UserServiceImpl implements UserRepository {
    private JdbcTemplate jdbcTemplate;

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (name, login, email, birthday) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getEmail());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    public Boolean exist(int id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM users WHERE id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    public User update(User user) {
        if (!exist(user.getId())) return null;
        String sql = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    public User get(Integer id) {
        if (!exist(id)) return null;
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new UserMapper(), id);
    }

    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    public List<User> findAllById(Set<Integer> userIds) {
        List<User> completeUsers = new ArrayList<>();
        if (!userIds.isEmpty()) {
            for (Integer id : userIds) {
                completeUsers.add(get(id));
            }
            log.info("Получен список пользователей в количестве: {}", completeUsers.size());
            return completeUsers;
        }
        log.warn("Пользователи отсутствуют");
        return new ArrayList<>();
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
