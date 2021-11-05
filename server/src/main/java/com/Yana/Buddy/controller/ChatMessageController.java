package com.Yana.Buddy.controller;

import com.Yana.Buddy.dto.ChatMessageRequestDto;
import com.Yana.Buddy.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    //메시지 요청 처리
    @MessageMapping("/chat/message")
    public void message(@RequestBody ChatMessageRequestDto dto) {
        chatMessageService.message(dto);
    }

}
