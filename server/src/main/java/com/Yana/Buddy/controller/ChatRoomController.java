package com.Yana.Buddy.controller;

import com.Yana.Buddy.dto.ChatRoomResponse;
import com.Yana.Buddy.entity.ChatMessage;
import com.Yana.Buddy.entity.ChatRoom;
import com.Yana.Buddy.service.ChatMessageService;
import com.Yana.Buddy.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    //모든 채팅방 조회
    @GetMapping("rooms")
    public List<ChatRoom> allChatRoom() {
        return chatRoomService.findAllRooms();
    }

    //채팅방 생성
    @PostMapping("/room")
    public ChatRoom createRoom() {
        return chatRoomService.createChatRoom();
    }

    //유저가 1명만 있는 방들 중에서 랜덤으로 채팅방 하나 뽑거나, 해당하는 방이 없을 경우 새로운 채팅방 배정
    @GetMapping("/room")
    public ResponseEntity<ChatRoomResponse> getAvailableRoom() {
        return chatRoomService.getAvailableRoom();
    }

    //채팅방 상세 조회
    @GetMapping("/room/{roomId}")
    public ChatRoom getRoomDetail(@PathVariable String roomId) {
        return chatRoomService.getRoomInfoByRoomId(roomId);
    }

    //roomId를 통해 채팅방 삭제
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity deleteRoomByRoomId(@PathVariable String roomId) {
        return chatRoomService.deleteRoomByRoomId(roomId);
    }

    //채팅방 메시지 전체 조회
    @GetMapping("/room/{roomId}/message")
    public List<ChatMessage> getAllMessageOfRoom(@PathVariable Long roomId) {
        return chatMessageService.getAllMessage(roomId);
    }

}
