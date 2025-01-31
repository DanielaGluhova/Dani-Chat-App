package com.example.danichat.controllers;
import com.example.danichat.entities.Friend;
import com.example.danichat.entities.User;
import com.example.danichat.http.AppResponse;
import com.example.danichat.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> createNewUser(@RequestBody User user) {

        if(this.userService.createUser(user)) {
            return AppResponse.success()
                    .withMessage("Customer created successfully")
                    .build();
        }

        return AppResponse.error()
                .withMessage("Customer could not be created")
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        User existingUser = userService.loginUser(user.getUsername(), user.getEmail());

        if (existingUser != null) {
            return AppResponse.success().withData(existingUser)
                    .withMessage("Login successful")
                    .build();
        } else {
            return AppResponse.error()
                    .withMessage("Invalid username or email")
                    .build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUserByName(@RequestParam String username) {
        List<User> users = userService.searchUserByName(username);

        if (!users.isEmpty()) {
            return AppResponse.success()
                    .withMessage("Users found successfully")
                    .withData(users)
                    .build();
        }

        return AppResponse.error()
                .withMessage("No users found with the given name")
                .build();
    }

    @PostMapping("/friends")
    public ResponseEntity<?> createNewFriend(@RequestBody Friend friend) {

        if(this.userService.createFriend(friend)) {
            return AppResponse.success()
                    .withMessage("Friend added successfully")
                    .build();
        }

        return AppResponse.error()
                .withMessage("Friend could not be added. Maybe is already in your friends list")
                .build();
    }

    @GetMapping("/friends/{userId}")
    public ResponseEntity<?> getAllFriends(@PathVariable int userId) {
        List<Map<String, Object>> friends = userService.getAllFriends(userId);

        if (!friends.isEmpty()) {
            return AppResponse.success()
                    .withData(friends)
                    .withMessage("Friends showed successfully")
                    .build();
        }

        return AppResponse.error()
                .withMessage("This user has no friends.")
                .build();
    }

    @GetMapping("/members/{channel_id}")
    public ResponseEntity<?> getAllMembers(@PathVariable int channel_id) {
        List<Map<String, Object>> members = userService.getAllMembers(channel_id);

        if (!members.isEmpty()) {
            return AppResponse.success()
                    .withData(members)
                    .withMessage("Friends showed successfully")
                    .build();
        }

        return AppResponse.error()
                .withMessage("This user has no friends.")
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDeleteUser(@PathVariable int id) {

        if(this.userService.softDeleteUser(id)) {
            return AppResponse.success()
                    .withMessage("Customer deleted successfully")
                    .build();
        }

        return AppResponse.error()
                .withMessage("Customer could not be deleted")
                .build();
    }
}
