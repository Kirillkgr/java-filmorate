package ru.yandex.practicum.filmorate.repository.Impl;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendshipRepository;

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public class FriendshipServiceImpl implements FriendshipRepository {
    JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(User parent, User child) {
        boolean isConfirmed = isFriendshipConfirmed(child, parent);
        String status = isConfirmed ? "confirmed" : "unconfirmed";

        String sql = "INSERT INTO friendship (user_id, friend_id, status) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
            ps.setInt(1, parent.getId());
            ps.setInt(2, child.getId());
            ps.setString(3, status);
            return ps;
        }, keyHolder);

        int newId = keyHolder.getKey().intValue();
        // Дополнительная логика, если требуется
    }

    @Override
    public void removeFriend(User parent, User child) {

    }

    @Override
    public Friendship getAll(User parent) {
        return null;
    }

    @Override
    public Boolean isFriendshipConfirmed(User parent, User child) {
        String checkSql = "SELECT COUNT(*) FROM friendship WHERE user_id = ? AND friend_id = ?";
        Optional<Integer> count = Optional.ofNullable(jdbcTemplate.queryForObject(checkSql,
                (rs, rowNum) -> rs.getInt("COUNT(*)"),
                parent.getId(), child.getId()));
        return count.isPresent() && count.get() > 0;
    }

    @Override
    public Boolean checkFriendship(User parent, User child) {
        return null;
    }

    @Override
    public Set<Integer> getFriends(Integer userId) {
        String sql = "SELECT FRIEND_ID FROM friendship WHERE user_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("FRIEND_ID"), userId));
    }
}
