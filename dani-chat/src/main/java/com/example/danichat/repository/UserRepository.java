package com.example.danichat.repository;

import com.example.danichat.entities.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean createUser(User user){
        try {
            String query = "INSERT INTO users (username, email) VALUES (?,?)";
            jdbcTemplate.update(query, user.getUsername(), user.getEmail());

            return true;
        } catch (Exception e) {
            System.err.println("Creating user gives this error: " + e.getMessage());

            return false;
        }
    }

    public User findByUsernameAndEmail(String username, String email) {
        String query = "SELECT * FROM users WHERE username = ? AND email = ? AND is_deleted = false";
        List<User> resultList = jdbcTemplate.query(
                query,
                new BeanPropertyRowMapper<>(User.class),
                username,
                email
        );

        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
    }

    public List<User> searchUserByName(String username) {
        String query = "SELECT id, username, email FROM users WHERE LOWER(username) LIKE ? AND is_deleted = false";
        RowMapper<User> rowMapper = (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            return user;
        };
        return jdbcTemplate.query(query, rowMapper, "%"+username+"%");
    }

    public String getUsernameById(int userId) {
        String query = "SELECT username FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(query, String.class, userId);
    }

    public boolean softDeleteUser(int id) {
        try{
            String query = "UPDATE users SET is_deleted = true WHERE id = ?";
            jdbcTemplate.update(query, id);

            return true;
        } catch (Exception e){
            System.err.println("Deleting user gives this error: " + e.getMessage());

            return false;
        }

    }
}
