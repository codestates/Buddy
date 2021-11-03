package com.Yana.Buddy.dto;

import lombok.Data;

@Data
public class ChatMessageRequestDto {

    private String type;
    private String roomId;
    private Long userId;
    private String sender;
    private String message;
    private String createdAt;

}
