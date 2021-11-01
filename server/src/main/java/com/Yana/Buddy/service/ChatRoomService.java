package com.Yana.Buddy.service;

import com.Yana.Buddy.dto.ChatRoomRequestDto;
import com.Yana.Buddy.entity.ChatRoom;
import com.Yana.Buddy.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOperations;

    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    private static final String ENTER_INFO = "ENTER_INFO";

    //채팅방 생성
    public ChatRoom createChatRoom(ChatRoomRequestDto dto) {
        ChatRoom chatRoom = new ChatRoom(dto, userService);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    //전체 채팅방 조회
    public List<ChatRoom> getAllChatRoom() {
        return chatRoomRepository.findAllByOrderByCreatedAtDesc();
    }

    //채팅방 주제별 조회
    public List<ChatRoom> getChatRoomBySubject(String subject) {
        return chatRoomRepository.findBySubject(subject);
    }

    //채팅방 개별 조회
    public ChatRoom getChatRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다."));
        return chatRoom;
    }

    public void setEnterUserInfo(String sessionId, String roomId) {
        hashOperations.put(ENTER_INFO, sessionId, roomId);
    }

    public String getEnterUserId(String sessionId) {
        return hashOperations.get(ENTER_INFO, sessionId);
    }

    public void removeEnterUserInfo(String sessionId) {
        hashOperations.delete(ENTER_INFO, sessionId);
    }

}
