package com.example.danichat.repository;
import com.example.danichat.entities.ChannelUser;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Arrays;
import java.util.List;

@Repository
public class ChannelUserRepository {
    private final JdbcTemplate jdbcTemplate;
    public ChannelUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public boolean createChannelUser(ChannelUser channelUser) {
        try {
            List<String> allowedRoles = Arrays.asList("OWNER", "ADMIN", "USER");
            if (!allowedRoles.contains(channelUser.getRole())) {
                System.err.println("Invalid role: " + channelUser.getRole());
                return false;
            }

            String checkQuery = "SELECT COUNT(*) FROM channel_users WHERE channel_id = ? AND user_id = ?";
            int count = jdbcTemplate.queryForObject(checkQuery, Integer.class, channelUser.getChannel_id(), channelUser.getUser_id());
            if (count > 0) {
                System.err.println("User already exists in the channel.");
                return false;
            }

            String query = "INSERT INTO channel_users (channel_id, user_id, role) VALUES (?, ?, ?)";
            jdbcTemplate.update(query, channelUser.getChannel_id(), channelUser.getUser_id(), channelUser.getRole());
            return true;

        } catch (DataIntegrityViolationException e) {
            System.err.println("SQL Constraint Violation: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error creating member of the channel: " + e.getMessage());
        }
        return false;
    }

    public String getRole(int channel_id, int user_id){
        try{
            String query = "SELECT role FROM channel_users WHERE channel_id = ? AND user_id = ?";
            return jdbcTemplate.queryForObject(query,String.class,channel_id,user_id);
        } catch (Exception e) {
            System.err.println("Error getting role:" + e.getMessage());
            return null;
        }
    }

    public boolean setAdminRoleToUser(int channel_id,int user_id){
        String query = "UPDATE channel_users SET role = 'ADMIN' WHERE channel_id = ? AND user_id = ?";
        jdbcTemplate.update(query,channel_id,user_id);
        return true;
    }


    public boolean deleteAllMembersFromChannelById(int id){
        String deleteUsersQuery = "UPDATE channel_users SET is_deleted = true WHERE channel_id = ? ";
        jdbcTemplate.update(deleteUsersQuery, id);

        return true;
    }


    public boolean removeUserFromChannel(int id, int user_id) {
        try {
            String query = "UPDATE channel_users SET is_deleted = true WHERE user_id = ? AND channel_id = ?";
            jdbcTemplate.update(query, user_id, id);
            return true;
        } catch (Exception e){
            System.err.println("Error removing member from channel: " + e.getMessage());
            return false;
        }
    }
}
