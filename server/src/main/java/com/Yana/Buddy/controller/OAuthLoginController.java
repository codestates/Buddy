package com.Yana.Buddy.controller;

import com.Yana.Buddy.dto.TokenResponse;
import com.Yana.Buddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OAuthLoginController {

    private final UserService userService;

    private static final String ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
    @Value("${oauth.google.client-id}")
    private String CLIENT_ID;
    private static final String REDIRECT_URI = "http://localhost:8080/oauth/google/callback";
    private static final String RESPONSE_TYPE = "code";
    private static final String SCOPE = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";

    @GetMapping("/login_google")
    public String GoogleLogin() {
        return "redirect:" + ENDPOINT + "?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=" + RESPONSE_TYPE + "&scope=" + SCOPE;
    }

    @GetMapping("/oauth/google/callback")
    public ResponseEntity<TokenResponse> oauthLogin(String code) {
        String token = userService.oauthLogin(code);
        return new ResponseEntity(new TokenResponse(token, "bearer"), HttpStatus.OK);
    }

}
