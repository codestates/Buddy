package com.Yana.Buddy.dto;

import lombok.Data;

@Data
public class RefreshTokenDto {

    private String email;
    private String refreshToken;

}
