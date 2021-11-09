package com.Yana.Buddy.dto;

import com.Yana.Buddy.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.servlet.http.Cookie;

@Data
@AllArgsConstructor
@Builder
public class OAuthLoginDto {

    private User user;
    private String accessToken;
    private String refreshToken;

}
