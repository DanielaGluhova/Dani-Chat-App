package com.example.danichat.services;
import com.example.danichat.repository.MessageRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public boolean sendMessageToChannel(int sender_id, int channel_id, String content) {
        return this.messageRepository.sendMessageToChannel(sender_id,channel_id,content);
    }

    public List<Map<String, Object>> getAllMessages(int channel_id) {
        return this.messageRepository.getMessagesByChannelId(channel_id);
    }
}
