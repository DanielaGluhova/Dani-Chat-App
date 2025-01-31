package com.example.danichat.services;
import com.example.danichat.entities.Friend;
import com.example.danichat.entities.User;
import com.example.danichat.repository.FriendRepository;
import com.example.danichat.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final ChannelService channelService;
    public UserService(UserRepository userRepository, FriendRepository friendRepository, ChannelService channelService) {
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
        this.channelService = channelService;
    }

    public boolean createUser(User user) {
        return this.userRepository.createUser(user);
    }

    public User loginUser(String username, String email) {
        return userRepository.findByUsernameAndEmail(username, email);
    }

    public List<User> searchUserByName(String username) {
        return userRepository.searchUserByName(username.toLowerCase());
    }

    public boolean createFriend(Friend friend) {
        if (this.friendRepository.createFriend(friend)) {
            String userName = userRepository.getUsernameById(friend.getUser_id());
            String friendName = userRepository.getUsernameById(friend.getFriend_id());

            channelService.createFriendChat(friend.getUser_id(), friend.getFriend_id(), friendName+"::"+userName);
            channelService.createFriendChat(friend.getFriend_id(), friend.getUser_id(), userName+"::"+friendName);

            return true;
        }
        return false;
    }

    public List<Map<String, Object>> getAllFriends(int userId) {
        return friendRepository.getAllFriendsByUserId(userId);
    }

    public List<Map<String, Object>> getAllMembers(int channel_id) {
        return friendRepository.getAllMembersByUserId(channel_id);
    }

    public boolean softDeleteUser(int id){
        return userRepository.softDeleteUser(id);
    }

}
