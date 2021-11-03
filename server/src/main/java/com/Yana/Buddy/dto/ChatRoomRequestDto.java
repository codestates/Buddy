package com.Yana.Buddy.dto;

import lombok.Data;

@Data
public class ChatRoomRequestDto {

    private Long userId;
    private String name;
    private String subject;
    private String image;

}
