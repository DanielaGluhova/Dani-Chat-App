package com.example.danichat.repository;
import com.example.danichat.entities.Friend;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class FriendRepository {
    private final JdbcTemplate jdbcTemplate;
    public FriendRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean createFriend(Friend friend){
        String query = "INSERT INTO friends (user_id, friend_id) SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM friends WHERE user_id = ? AND friend_id = ?)";
        int rowsAffected = jdbcTemplate.update(query,friend.getUser_id(),friend.getFriend_id(),friend.getUser_id(),friend.getFriend_id());

        return rowsAffected>0;
    }

    public List<Map<String, Object>> getAllFriendsByUserId(int userId) {
        String sql = "SELECT DISTINCT u.id AS friendId, u.username AS friendName, u.email AS friendEmail \n" +
                "FROM friends f \n" +
                "JOIN users u ON f.friend_id = u.id \n" +
                "JOIN channels c ON c.name LIKE '%::%' \n" +
                "WHERE f.user_id = ? AND f.is_deleted = false;\n";

        return jdbcTemplate.queryForList(sql, userId);
    }

    public List<Map<String, Object>> getAllMembersByUserId(int channel_id) {
        String sql = "SELECT c.name AS channel_name, u.username AS user_name, cu.role AS user_role, u.id AS user_id\n" +
                "FROM channel_users cu\n" +
                "JOIN channels c ON cu.channel_id = c.id\n" +
                "JOIN users u ON cu.user_id = u.id\n" +
                "WHERE cu.channel_id = ? AND cu.is_deleted = false AND u.is_deleted = false AND c.is_deleted = false";

        return jdbcTemplate.queryForList(sql, channel_id);
    }
}
