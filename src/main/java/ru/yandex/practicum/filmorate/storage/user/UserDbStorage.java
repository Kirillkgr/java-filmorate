package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.DTO.FriendDTO;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
    public User getUser(Integer id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
    }

    @Override
    public User updateUser(User updateUser) {
        if (!existsById(updateUser.getId())) return null;
        String sql = "UPDATE users SET name = ?, email = ?, login = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sql, updateUser.getName(), updateUser.getEmail(), updateUser.getLogin(), updateUser.getBirthday(), updateUser.getId());
        return getUser(updateUser.getId());
    }

    @Override
    public User createUser(User newUser) {
        String sql = "INSERT INTO users (name, email, login, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, newUser.getName());
            ps.setString(2, newUser.getEmail());
            ps.setString(3, newUser.getLogin());
            ps.setDate(4, Date.valueOf(newUser.getBirthday()));
            return ps;
        }, keyHolder); // Устанавливаем сгенерированный ключ
        newUser.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return newUser;
    }

    @Override
    public boolean addFriend(Integer id, Integer friendId) {
        // Проверяем, существует ли запись о дружбе между user_id и friend_id
        String checkSql = "SELECT status FROM friendship WHERE user_id = ? AND friend_id = ?";
        List<String> statusListUser = jdbcTemplate.query(checkSql, (rs, rowNum) -> rs.getString("status"), id, friendId);

        // Проверяем, существует ли обратная запись о дружбе
        List<String> reverseStatusList = jdbcTemplate.query(checkSql, (rs, rowNum) -> rs.getString("status"), friendId, id);

        // Логирование для отладки
        System.out.println("Status list for user_id=" + id + " and friend_id=" + friendId + ": " + statusListUser);
        System.out.println("Reverse status list for user_id=" + friendId + " and friend_id=" + id + ": " + reverseStatusList);

        if (statusListUser.isEmpty()) {
            // Вставляем новую запись со статусом "pending"
            String insertSql = "INSERT INTO friendship (user_id, friend_id, status) VALUES (?, ?, 'pending')";
            jdbcTemplate.update(insertSql, id, friendId);
            System.out.println("Friendship added: userId=" + id + ", friendId=" + friendId + ", status=pending");
        }

        if (reverseStatusList.contains("pending")) {
            // Обновляем обе записи на "accepted" при взаимном добавлении
            String updateSql = "UPDATE friendship SET status = 'accepted' WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
            int updatedRows = jdbcTemplate.update(updateSql, id, friendId, friendId, id);
            System.out.println("Number of rows updated: " + updatedRows);
            System.out.println("Friendship accepted: userId=" + id + ", friendId=" + friendId);
        }
        return true;
    }


    public boolean acceptFriendship(Integer id, Integer friendId) {
        try {
            String checkSql = "SELECT COUNT(*) FROM friendship WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?) AND status like '%pending%'";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, id, friendId, friendId, id);

            if (count != null && count == 2) {
                // Обновляем статус на "accepted" для обеих записей
                String updateSql = "UPDATE friendship SET status = 'accepted' WHERE (user_id = ? AND friend_id = ?) OR (friend_id = ? AND user_id = ?)";
                jdbcTemplate.update(updateSql, id, friendId, friendId, id);
                System.out.println("Friendship accepted: userId=" + id + ", friendId=" + friendId);
                return true;
            } else {
                System.out.println("Friendship not found or not both pending: userId=" + id + ", friendId=" + friendId);
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error accepting friendship: userId=" + id + ", friendId=" + friendId);
            e.printStackTrace();
            return false;
        }
    }
//    @Override
//    public boolean addFriend(Integer id, Integer friendId) {
//        try {
//            // Проверяем, существует ли уже запись о дружбе с нужными статусами
//            String checkSql = "SELECT COUNT(*) FROM friendship WHERE user_id = ? AND friend_id = ?";
//            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, id, friendId);
//
//            if (count != null && count > 0) {
//                // Запись существует, ничего не делаем
//                System.out.println("Friendship already exists: userId=" + id + ", friendId=" + friendId);
//                return true;
//            } else {
//                // Вставляем новую запись с "pending" статусом только для user1 -> user2
//                String insertSql = "INSERT INTO friendship (user_id, friend_id, status) VALUES (?, ?, 'pending')";
//                jdbcTemplate.update(insertSql, id, friendId);
//                System.out.println("Friendship added: userId=" + id + ", friendId=" + friendId + ", status=pending");
//            }
//        } catch (Exception e) {
//            System.err.println("Error adding friend: userId=" + id + ", friendId=" + friendId);
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        String sql = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        try {
            int rows = jdbcTemplate.update(sql, id, friendId);
        } catch (Exception e) {
            System.err.println("Error deleting friend: userId=" + id + ", friendId=" + friendId);
            e.printStackTrace();
        }
    }


    @Override
    public List<FriendDTO> getFriends(Integer id) {
        String sql = "SELECT u.id, u.name, f.status FROM users u " + "JOIN friendship f ON u.id = f.friend_id " + "WHERE f.user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            FriendDTO friendDTO = new FriendDTO();
            friendDTO.setId(rs.getInt("id"));
            friendDTO.setName(rs.getString("name"));
            friendDTO.setStatus(rs.getString("status"));
            return friendDTO;
        }, id);
    }


    private String getFriendshipStatus(Integer userId, Integer friendId) {
        String sql = "SELECT status FROM friendship WHERE user_id = ? AND friend_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId, friendId}, String.class);
    }

    @Override
    public List<FriendDTO> getCommonFriends(Integer id, Integer otherId) {
        String sql = """
                    SELECT DISTINCT u.id, u.name
                    FROM users u
                    JOIN friendship f1 ON u.id = f1.friend_id
                    JOIN friendship f2 ON u.id = f2.friend_id
                    WHERE f1.user_id = ? AND f2.user_id = ?
                """;
        return jdbcTemplate.query(sql, this::mapRowToFriendDTO, id, otherId);
    }

    private FriendDTO mapRowToFriendDTO(ResultSet rs, int rowNum) throws SQLException {
        return new FriendDTO(rs.getInt("id"), rs.getString("name"), "");
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            return user;
        }
    }
}