package com.example.danichat.repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class MessageRepository {
    private final JdbcTemplate jdbcTemplate;
    public MessageRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean sendMessageToChannel(int sender_id, int channel_id, String content) {
        try {
            String query = "INSERT INTO messages (sender_id, channel_id, content) VALUES (?,?,?)";
            jdbcTemplate.update(query,sender_id,channel_id,content);

            return true;
        } catch (Exception e) {
            System.err.println("Sending message gives this error: " + e.getMessage());

            return false;
        }
    }

    public List<Map<String, Object>> getMessagesByChannelId(int channelId) {
        String query = "SELECT * FROM messages WHERE channel_id = ? AND is_deleted = false ORDER BY sent_at DESC";
        return jdbcTemplate.queryForList(query, channelId);
    }
}
