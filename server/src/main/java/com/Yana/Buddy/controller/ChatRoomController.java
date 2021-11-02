package com.Yana.Buddy.controller;

import com.Yana.Buddy.dto.ChatRoomRequestDto;
import com.Yana.Buddy.entity.ChatMessage;
import com.Yana.Buddy.entity.ChatRoom;
import com.Yana.Buddy.service.ChatMessageService;
import com.Yana.Buddy.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    //채팅방 생성
    @PostMapping("/room")
    public ChatRoom createChatRoom(@RequestBody ChatRoomRequestDto dto) {
        return chatRoomService.createChatRoom(dto);
    }

    //채팅방 전체 조회
    @GetMapping("/rooms")
    public List<ChatRoom> getAllChatRoom() {
        return chatRoomService.getAllChatRoom();
    }

    //채팅방 주제별 조회
    @GetMapping("/rooms/{subject}")
    public List<ChatRoom> getChatRoomBySubject(@PathVariable String subject) {
        return chatRoomService.getChatRoomBySubject(subject);
    }

    //채팅방 상세 조회
    @GetMapping("/room/{roomId}")
    public ChatRoom getChatRoom(@PathVariable Long roomId) {
        return chatRoomService.getChatRoom(roomId);
    }

    //해당 채팅방 메시지 전체 조회
    @GetMapping("/room/messages/{roomId}")
    public Page<ChatMessage> getChatRoomMessages(@PathVariable String roomId, @PageableDefault Pageable pageable) {
        return chatMessageService.getChatMessageByRoomId(roomId, pageable);
    }

}
