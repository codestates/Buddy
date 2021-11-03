package com.Yana.Buddy.dto;

import com.Yana.Buddy.entity.ChatMessage;
import lombok.Data;

@Data
public class ChatMessageRequestDto {

    private ChatMessage.MessageType type;
    private Long roomId;
    private Long userId;
    private String sender;
    private String message;
    private String createdAt;

}
