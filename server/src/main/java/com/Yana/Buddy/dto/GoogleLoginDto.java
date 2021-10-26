package com.Yana.Buddy.dto;

import com.Yana.Buddy.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.servlet.http.Cookie;

@Data
@AllArgsConstructor
public class GoogleLoginDto {

    private User user;
    private String accessToken;
    private String refreshToken;
    private Cookie cookie;

}
