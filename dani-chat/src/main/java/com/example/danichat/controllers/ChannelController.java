package com.example.danichat.controllers;
import com.example.danichat.dto.Channel;
import com.example.danichat.dto.MemberDTO;
import com.example.danichat.http.AppResponse;
import com.example.danichat.repository.ChannelRepository;
import com.example.danichat.services.ChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ChannelController {
    private final ChannelService channelService;
    private final ChannelRepository channelRepository;

    public ChannelController(ChannelService channelService, ChannelRepository channelRepository) {
        this.channelService = channelService;
        this.channelRepository = channelRepository;
    }

    @PostMapping("/channels")
    public ResponseEntity<?> createNewChannel(@RequestBody Channel channel) {
        HashMap<String, Object> response = new HashMap<>();

        if(this.channelService.createChannel(channel)) {
            return AppResponse.success()
                    .withMessage("Channel created successfully")
                    .build();
        }

        return AppResponse.error()
                .withMessage("Channel could not be created")
                .build();
    }

    @PostMapping("/channels/{id}")
    public ResponseEntity<?> addUserToChannel(@PathVariable int id, @RequestBody MemberDTO member) {
        member.setChannel_id(id);

        if (this.channelService.addMemberToChannel(member)) {
            return AppResponse.success()
                    .withMessage("Member created successfully")
                    .build();
        }

        return AppResponse.error()
                .withMessage("Member could not be created")
                .build();
    }

    @PutMapping("/channels/{id}")
    public ResponseEntity<?> renameChannel(@PathVariable int id, @RequestBody Channel channel, @RequestParam(name="owner_id") int owner_id) {
        channel.setOwner_id(owner_id);

        if(!this.channelService.renameChannel(id,channel)) {
            return AppResponse.error()
                    .withMessage("Could not rename the channel")
                    .build();
        }

        return AppResponse.success()
                .withMessage("Channel successfully renamed")
                .build();
    }

    @PutMapping("/channels/{id}/admin-role")
    public ResponseEntity<?> getAdminRoleToUser(
            @PathVariable int id,
            @RequestParam(name = "owner_id") int owner_id,
            @RequestParam(name = "user_id") int user_id) {

        if (!this.channelService.setRoleAdminToUser(id,owner_id, user_id)) {
            return AppResponse.error()
                    .withMessage("Could not set role ADMIN to user")
                    .build();
        }

        return AppResponse.success()
                .withMessage("Role is successfully changed to ADMIN")
                .build();
    }

    @DeleteMapping("channels/{id}/{user_id}")
    public AppResponse removeUserFromChannel(
            @PathVariable int id,
            @PathVariable(name = "user_id") int user_id,
        @RequestParam(name = "owner_id") int owner_id) {

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setChannel_id(id);
        memberDTO.setOwner_id(owner_id);
        memberDTO.setUser_id(user_id);

            if (this.channelService.removeUserFromChannel(memberDTO)) {
                return AppResponse.success().withMessage("User removed from the channel successfully");
            } else {
                return AppResponse.error().withMessage("Failed to remove user from the channel");
            }
    }

    @GetMapping("/channels/member/{userId}")
    public ResponseEntity<?> getAllChannels(@PathVariable int userId) {
        List<Map<String, Object>> channels = channelService.getAllChannels(userId);

        if (!channels.isEmpty()) {
            return AppResponse.success()
                    .withData(channels)
                    .withMessage("Channels showed successfully")
                    .build();
        }

        return AppResponse.error()
                .withMessage("This user has no channels.")
                .build();
    }

    @DeleteMapping("/channels/{id}")
    public ResponseEntity<?> deleteChannel(@PathVariable int id, @RequestParam(name = "owner_id") int owner_id) {

        if (!this.channelService.deleteChannel(id, owner_id)) {
            return AppResponse.error()
                    .withMessage("Channel not found or you are not the owner")
                    .build();
        }

        return AppResponse.success()
                .withMessage("Channel removed successfully")
                .build();
    }



}
