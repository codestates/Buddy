package com.Yana.Buddy.controller;

import com.Yana.Buddy.dto.ChatMessageRequestDto;
import com.Yana.Buddy.entity.ChatMessage;
import com.Yana.Buddy.entity.ChatRoom;
import com.Yana.Buddy.service.ChatMessageService;
import com.Yana.Buddy.service.ChatRoomService;
import com.Yana.Buddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final UserService userService;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat/message")
    public void message(@RequestBody ChatMessageRequestDto dto) {
        //유저 아이디를 통해 닉네임을 찾은 후, sender 로 지정
        dto.setSender(userService.findUserById(dto.getUserId()).getNickname());

        //메시지 생성 시간 삽입
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String dateResult = sdf.format(date);
        dto.setCreatedAt(dateResult);

        //roomId 를 통해 채팅방 정보 획득
        ChatRoom chatRoom = chatRoomService.getRoomInfoByRoomId(dto.getRoomId());

        ChatMessage message = ChatMessage.builder()
                .room(chatRoom)
                .type(dto.getType())
                .roomId(dto.getRoomId())
                .sender(dto.getSender())
                .message(dto.getMessage())
                .createdAt(dto.getCreatedAt())
                .build();

        //WebSocket 을 통해 채팅방 구독자들에게 메시지 전송
        chatMessageService.sendChatMessage(message);

        //DB 에도 채팅 메시지 저장
        chatMessageService.save(message);
    }

}
