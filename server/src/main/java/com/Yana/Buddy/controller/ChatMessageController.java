package com.Yana.Buddy.controller;

import com.Yana.Buddy.dto.ChatMessageRequestDto;
import com.Yana.Buddy.entity.ChatMessage;
import com.Yana.Buddy.service.ChatMessageService;
import com.Yana.Buddy.service.UserService;
import lombok.RequiredArgsConstructor;
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

    /*
    클라이언트로부터 채팅 메시지 받기
    웹 소켓으로 publish 된 메시지를 받음
     */
    @MessageMapping("/chat/message")
    public void message(@RequestBody ChatMessageRequestDto dto) {
        dto.setSender(userService.findUserById(dto.getChatUserId()).getNickname());

        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String dateResult = sdf.format(date);
        dto.setCreatedAt(dateResult);

        ChatMessage message = new ChatMessage(dto, userService);

        //웹 소켓 통신으로 채팅방 토픽 구독자들에게 메시지 전송
        chatMessageService.sendChatMessage(message);

        //MySQL DB에 채팅 메시지 저장
        chatMessageService.save(message);
    }

}
