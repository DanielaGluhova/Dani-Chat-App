package com.example.danichat.controllers;
import com.example.danichat.http.AppResponse;
import com.example.danichat.services.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;

@Controller
public class MessageController {
    private final MessageService messageService;
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessageToChannel(@RequestParam(name="sender_id") int sender_id,
                                                  @RequestParam(name="channel_id") int channel_id,
                                                  @RequestParam(name="content") String content){

        if(this.messageService.sendMessageToChannel(sender_id, channel_id, content)){
            return AppResponse.success()
                    .withMessage("Message sent successfully")
                    .build();
        }
        return AppResponse.error()
                .withMessage("Message could not be sent")
                .build();

    }

    @GetMapping("/messages/{channel_id}")
    public ResponseEntity<?> getAllMessages(@PathVariable int channel_id) {
        List<Map<String, Object>> messages = messageService.getAllMessages(channel_id);

        if (!messages.isEmpty()) {
            return AppResponse.success().withData(messages)
                    .withMessage("Messages showed successfully")
                    .build();
        }

        return AppResponse.error()
                .withMessage("This channel has no messages.")
                .build();
    }
}
