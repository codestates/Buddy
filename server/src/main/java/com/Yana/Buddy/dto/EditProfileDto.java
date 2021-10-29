package com.Yana.Buddy.dto;

import lombok.Data;

@Data
public class EditProfileDto {

    private String nickname;
    private String password;
    private String stateMessage;
    private String profile_image;

}
