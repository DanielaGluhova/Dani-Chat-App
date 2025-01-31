package com.example.danichat.repository;

import com.example.danichat.entities.Channel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ChannelRepository {
    private final JdbcTemplate jdbcTemplate;

    public ChannelRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createChannel(Channel channel){
        String query = "INSERT INTO channels (name) VALUES (?)";
        jdbcTemplate.update(query,channel.getName());

        String getChannelIdQuery = "SELECT id FROM channels WHERE name = ? AND is_deleted = false";
        return jdbcTemplate.queryForObject(getChannelIdQuery, Integer.class, channel.getName());
    }

    public boolean renameChannel(int id,String name){
        String renameChannelQuery = "UPDATE channels SET name = ? WHERE id = ? AND is_deleted = false";

        if(jdbcTemplate.update(renameChannelQuery,name,id)>0){
            return true;
        }
        return false;
    }

    public boolean deleteChannel(int id){
        String deleteChannelQuery = "UPDATE channels SET is_deleted = true WHERE id = ? AND is_deleted = false";
        jdbcTemplate.update(deleteChannelQuery,id);

        return true;
    }

    public List<Map<String, Object>> getAllChannels(int userId) {
        String sql = "SELECT c.id, c.name " +
        "FROM channels c JOIN channel_users cu ON c.id = cu.channel_id WHERE cu.user_id = ?" +
                " AND c.is_deleted = false";
        return jdbcTemplate.queryForList(sql, userId);
    }
}