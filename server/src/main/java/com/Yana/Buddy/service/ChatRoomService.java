package com.Yana.Buddy.service;

import com.Yana.Buddy.entity.ChatRoom;
import com.Yana.Buddy.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private static final long serialVersionUID = 6494678977089006639L;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOperations;

    private final ChatRoomRepository chatRoomRepository;
    public static final String ENTER_INFO = "ENTER_INFO"; //채팅방에 들어간 클라이언트의 sessionId 및 채팅방 id 매핑 정보 저장

    //채팅방 전체 조회
    public List<ChatRoom> findAllRooms() {
        return chatRoomRepository.findAll();
    }

    //채팅방 생성
    public ChatRoom createChatRoom() {
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .userCount(0)
                .build();
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }

    //채팅방 상세 조회
    public ChatRoom getRoomInfo(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );
    }

    //유저가 입장한 채팅방 id 및 유저 세션 id 정보 저장
    public void setUserEnterInfo(String sessionId, String roomId) {
        hashOperations.put(ENTER_INFO, sessionId, roomId);
    }

    //유저 세션으로 입장해 있는 채팅방 id 조회
    public String getUserEnterRoomId(String sessionId) {
        return hashOperations.get(ENTER_INFO, sessionId);
    }

    //유저 세션 정보와 매핑된 채팅방 id 삭제
    public void removeUserEnterInfo(String sessionID) {
        hashOperations.delete(ENTER_INFO, sessionID);
    }

    public ChatRoom getRoomInfoByRoomId(String roomId) {
        return chatRoomRepository.findByRoomId(roomId);
    }

    public ChatRoom getRoomInfoById(Long id) {
        return chatRoomRepository.findById(id).get();
    }

}
