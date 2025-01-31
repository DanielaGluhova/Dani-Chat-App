package com.example.danichat.services;
import com.example.danichat.dto.Channel;
import com.example.danichat.dto.MemberDTO;
import com.example.danichat.entities.ChannelUser;
import com.example.danichat.repository.ChannelRepository;
import com.example.danichat.repository.ChannelUserRepository;
import com.example.danichat.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final ChannelUserRepository channelUserRepository;
    private final UserRepository userRepository;

    public ChannelService(ChannelRepository channelRepository, ChannelUserRepository channelUserRepository, UserRepository userRepository) {
        this.channelRepository = channelRepository;
        this.channelUserRepository = channelUserRepository;
        this.userRepository = userRepository;
    }

    public boolean createChannel(Channel channel) {
        com.example.danichat.entities.Channel channelData = new com.example.danichat.entities.Channel();
        channelData.setName(channel.getName());
        int channelID = this.channelRepository.createChannel(channelData);

        ChannelUser channelOwner = new ChannelUser();
        channelOwner.setChannel_id(channelID);
        channelOwner.setUser_id(channel.getOwner_id());
        channelOwner.setRole("OWNER");

        return this.channelUserRepository.createChannelUser(channelOwner);
    }

    public void createFriendChat(int ownerId, int friendId, String channelName) {
        if (!channelName.contains("::")) {
            throw new IllegalArgumentException("Friend channel name must contain '::' to distinguish friend channels.");
        }

        com.example.danichat.entities.Channel channel = new com.example.danichat.entities.Channel();
        channel.setName(channelName);
        int channelId = channelRepository.createChannel(channel);

        addFriendToChannel(channelId, ownerId, "USER");
        addFriendToChannel(channelId, friendId, "USER");
    }

    private void addFriendToChannel(int channelId, int userId, String role) {
        ChannelUser channelUser = new ChannelUser();
        channelUser.setChannel_id(channelId);
        channelUser.setUser_id(userId);
        channelUser.setRole(role);
        channelUserRepository.createChannelUser(channelUser);
    }

    public boolean addMemberToChannel(MemberDTO member) {
        ChannelUser channelMember = new ChannelUser();
        channelMember.setChannel_id(member.getChannel_id());
        channelMember.setUser_id(member.getUser_id());

        String role = channelUserRepository.getRole(member.getChannel_id(),member.getOwner_id());
        if(Objects.equals(role, "OWNER")){
            channelMember.setRole(member.getRole());
        }else if(Objects.equals(role,"ADMIN")){
            channelMember.setRole("USER");
        }else {
            return false;
        }

        return this.channelUserRepository.createChannelUser(channelMember);
    }

    public boolean renameChannel(int id,Channel channel){

        String role = channelUserRepository.getRole(id, channel.getOwner_id());
        if(!Objects.equals(role,"OWNER")){
            return false;
        }

        return this.channelRepository.renameChannel(id, channel.getName());
    }

    public boolean setRoleAdminToUser(int id,int owner_id, int user_id){

        String role = channelUserRepository.getRole(id,owner_id);

        if(Objects.equals(role,"OWNER")){
            return this.channelUserRepository.setAdminRoleToUser(id,user_id);
        }
        return false;

    }

    public boolean removeUserFromChannel(MemberDTO member) {
        String role = channelUserRepository.getRole(member.getChannel_id(), member.getOwner_id());
        if(!Objects.equals(role, "OWNER")){
            return false;
        }

        return channelUserRepository.removeUserFromChannel(member.getChannel_id(), member.getUser_id());
    }


    public boolean deleteChannel(int id, int owner_id) {
        ChannelUser channelMember = new ChannelUser();
        channelMember.setChannel_id(id);
        channelMember.setUser_id(owner_id);

        String role = channelUserRepository.getRole(id,owner_id);
        if(!Objects.equals(role, "OWNER")){
            return false;
        }

        channelUserRepository.deleteAllMembersFromChannelById(id);
        return this.channelRepository.deleteChannel(id);
    }

    public List<Map<String, Object>> getAllChannels(int userId) {
        return channelRepository.getAllChannels(userId);
    }
}
