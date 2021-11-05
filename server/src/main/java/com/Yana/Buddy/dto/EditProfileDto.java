package com.Yana.Buddy.dto;

import lombok.Data;

@Data
public class EditProfileDto {

    private String nickname;
    private String password;
    private String state_message;
    private String profile_image;

}
