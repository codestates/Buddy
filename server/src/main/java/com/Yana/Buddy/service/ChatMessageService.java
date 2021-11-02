package com.Yana.Buddy.service;

import com.Yana.Buddy.entity.ChatMessage;
import com.Yana.Buddy.repository.ChatMessageRepository;
import com.Yana.Buddy.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

    //destination 정보에서 roomId 추출
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1) {
            return destination.substring(lastIndex + 1);
        }
        else return "";
    }

    //채팅방에 메시지 발송
    public void sendChatMessage(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장했습니다.");
            message.setSender("[알림]");
        } else if (ChatMessage.MessageType.QUIT.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 퇴장했습니다.");
            message.setSender("[알림]");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }

    public void save(ChatMessage message) {
        ChatMessage msg = new ChatMessage();
        msg.setType(message.getType());
        msg.setRoomId(msg.getRoomId());
        msg.setUser(userService.findUserById(msg.getChatUserId()));
        msg.setChatUserId(msg.getChatUserId());
        msg.setSender(message.getSender());
        msg.setMessage(message.getMessage());
        msg.setCreatedAt(message.getCreatedAt());
        chatMessageRepository.save(message);
    }

    public Page<ChatMessage> getChatMessageByRoomId(String roomId, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 150);
        return chatMessageRepository.findByRoomId(roomId, pageable);
    }

}
