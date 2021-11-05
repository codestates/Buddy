package com.Yana.Buddy.controller;

import com.Yana.Buddy.entity.ChatMessage;
import com.Yana.Buddy.entity.ChatRoom;
import com.Yana.Buddy.service.ChatMessageService;
import com.Yana.Buddy.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
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

    //유저가 1명만 있는 방들 중에서 랜덤으로 채팅방 하나 뽑기
    @GetMapping
    public ChatRoom getAvailableRoom() {
        //return chatRoomService.getAvailableRoom();
        return null;
    }

    //채팅방 상세 조회
    @GetMapping("/room/{roomId}")
    public ChatRoom getRoomDetail(@PathVariable String roomId) {
        return chatRoomService.getRoomInfoByRoomId(roomId);
    }

    //채팅방 메시지 전체 조회
    @GetMapping("/room/{roomId}/message")
    public List<ChatMessage> getAllMessageOfRoom(@PathVariable Long roomId) {
        return chatMessageService.getAllMessage(roomId);
    }

}
