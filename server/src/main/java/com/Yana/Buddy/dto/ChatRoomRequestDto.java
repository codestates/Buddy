package com.Yana.Buddy.dto;

import lombok.Data;

@Data
public class ChatRoomRequestDto {

    private String name;
    private String image;
    private String subject;
    private Long userId;

}
