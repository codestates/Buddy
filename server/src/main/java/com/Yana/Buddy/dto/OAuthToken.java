package com.Yana.Buddy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OAuthToken {

    private String access_token;
    private String expires_in;
    private String id_token;
    private String refresh_token;
    private String scope;
    private String token_type;

}
