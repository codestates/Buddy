package com.Yana.Buddy.service;

import com.Yana.Buddy.dto.ChatMessageRequestDto;
import com.Yana.Buddy.entity.ChatMessage;
import com.Yana.Buddy.entity.ChatRoom;
import com.Yana.Buddy.repository.ChatMessageRepository;
import com.Yana.Buddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1) {
            return destination.substring(lastIndex + 1);
        } else return "";
    }

    public void message(ChatMessageRequestDto dto) {
        //유저 아이디를 통해 닉네임을 찾은 후, sender 로 지정
        dto.setSender(userRepository.findById(dto.getUserId()).get().getNickname());

        //메시지 생성 시간 삽입
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String dateResult = sdf.format(date);
        dto.setCreatedAt(dateResult);

        ChatMessage message = ChatMessage.builder()
                .type(ChatMessage.MessageType.valueOf(dto.getType()))
                .roomId(dto.getRoomId())
                .sender(dto.getSender())
                .message(dto.getMessage())
                .createdAt(dto.getCreatedAt())
                .build();

        //WebSocket 을 통해 채팅방 구독자들에게 메시지 전송
        sendChatMessage(message);

        //DB 에도 채팅 메시지 저장
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

    public List<ChatMessage> getAllMessage(Long roomId) {
        return chatMessageRepository.findByRoomId(roomId);
    }

    public void deleteByRoomId(String roomId) {
        chatMessageRepository.deleteByRoomId(roomId);
    }

}
