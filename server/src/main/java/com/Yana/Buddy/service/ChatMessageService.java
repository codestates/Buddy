package com.Yana.Buddy.service;

import com.Yana.Buddy.entity.ChatMessage;
import com.Yana.Buddy.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChatMessageRepository chatMessageRepository;

    //destination 정보에서 roomId 추출
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1) {
            return destination.substring(lastIndex + 1);
        } else return "";
    }

    public List<ChatMessage> getAllMessage(Long roomId) {
        return chatMessageRepository.findByRoomId(roomId);
    }

    public void save(ChatMessage message) {
        chatMessageRepository.save(message);
    }

    public void sendChatMessage(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + " 님이 입장했어요! 🤩");
            message.setSender("[알림]");
        }

        else if (ChatMessage.MessageType.QUIT.equals(message.getType())) {
            message.setMessage(message.getSender() + " 님이 퇴장했어요... 🥺");
            message.setSender("[알림]");
        }

        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }

}
