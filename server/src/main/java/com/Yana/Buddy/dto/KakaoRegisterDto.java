package com.Yana.Buddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KakaoRegisterDto {

    private String email;
    private String nickname;
    private String profile_image;
    private String gender;

}
