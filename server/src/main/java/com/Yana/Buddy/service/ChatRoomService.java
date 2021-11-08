package com.Yana.Buddy.service;

import com.Yana.Buddy.dto.ChatRoomResponse;
import com.Yana.Buddy.entity.ChatRoom;
import com.Yana.Buddy.handler.ResponseEntityHandler;
import com.Yana.Buddy.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private static final long serialVersionUID = 6494678977089006639L;
    public static final String ENTER_INFO = "ENTER_INFO"; //채팅방에 들어간 클라이언트의 sessionId 및 채팅방 id 매핑 정보 저장
    Random random = new Random();

    private final ChatRoomRepository chatRoomRepository;
    private final ResponseEntityHandler responseHandler;

    //채팅방 전체 조회
    public List<ChatRoom> findAllRooms() {
        return chatRoomRepository.findAll();
    }

    /**
     1. 유저 카운트가 1인 방 찾기
     2-1. 해당하는 방이 없을 경우, 새로운 방을 만들고 방의 정보 리턴
     2-2. 해당하는 방들이 있는 경우, 해당 방들 중 랜덤으로 하나를 뽑아서 그 채팅방의 정보를 리턴
     */
    //유저가 1명만 있는 방들을 찾고, 그 중 하나만 랜덤으로 뽑기
    public ResponseEntity<ChatRoomResponse> getAvailableRoom() {
        List<ChatRoom> targets = chatRoomRepository.findByUserCount(1);

        if (targets.size() == 0) {
            ChatRoom chatRoom = createChatRoom();
            return responseHandler.chatRoomInfo(ChatRoomResponse.builder()
                            .roomId(chatRoom.getRoomId())
                            .user_count(chatRoom.getUserCount())
                            .message("대기 중인 방이 없으므로 새로운 방을 생성했습니다.")
                            .build());
        } else {
            ChatRoom chatRoom = targets.get(random.nextInt(targets.size()));
            return responseHandler.chatRoomInfo(ChatRoomResponse.builder()
                            .roomId(chatRoom.getRoomId())
                            .user_count(chatRoom.getUserCount())
                            .message("대기 중이던 유저와 랜덤 매칭되었습니다.")
                            .build());
        }
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

    //고유 id를 통한 채팅방 조회
    public ChatRoom getRoomInfo(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(
                () -> new IllegalArgumentException("해당 채팅방이 존재하지 않습니다.")
        );
    }

    //room id 값을 통한 채팅방 조회
    public ChatRoom findByRoomId(String roomId) {
        return chatRoomRepository.findByRoomId(roomId);
    }

    public void save(ChatRoom updatedRoom) {
        chatRoomRepository.save(updatedRoom);
    }

    //roomId를 통해 채팅방 삭제
    public ResponseEntity deleteRoomByRoomId(String roomId) {
        chatRoomRepository.deleteByRoomId(roomId);
        return responseHandler.singleSuccessResponse("채팅방이 삭제되었습니다.");
    }

    //entity 자체를 통해 채팅방 삭제
    public void delete(ChatRoom room) {
        chatRoomRepository.delete(room);
    }

}
