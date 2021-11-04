package com.Yana.Buddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginSuccessResponse {

    private Long id;
    private String email;
    private String nickname;
    private String accessToken;
    private String refreshToken;
    private String message;

}
