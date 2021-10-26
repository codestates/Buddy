package com.Yana.Buddy.controller;

import com.Yana.Buddy.dto.TokenResponse;
import com.Yana.Buddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@CrossOrigin(
        origins = "https://yana-buddy.com, " +
                "http://bucket-yana-buddy.s3-website.ap-northeast-2.amazonaws.com, " +
                "https://accounts.google.com, https://www.googleapis.com, " +
                "https://kauth.kakao.com",
        allowedHeaders = "*",
        allowCredentials = "true"
)
public class OAuthLoginController {

    private final UserService userService;

    private static final String REDIRECT_URI = "http://bucket-yana-buddy.s3-website.ap-northeast-2.amazonaws.com";
    private static final String RESPONSE_TYPE = "code";

    private static final String G_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
    @Value("${oauth.google.client-id}") private String G_CLIENT_ID;
    private static final String G_SCOPE = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
    private static final String G_ACCESS_TYPE = "offline";

    private static final String K_ENDPOINT = "https://kauth.kakao.com/oauth/authorize";
    @Value("${oauth.kakao.client-id}") private String K_CLIENT_ID;

    @GetMapping("/login_google")
    public String GoogleLogin() {
        return "redirect:" + G_ENDPOINT + "?client_id=" + G_CLIENT_ID + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=" + RESPONSE_TYPE + "&scope=" + G_SCOPE + "&access_type=" + G_ACCESS_TYPE;
    }

    @GetMapping("/login_kakao")
    public String KakaoLogin() {
        return "redirect:" + K_ENDPOINT + "?client_id=" + K_CLIENT_ID + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=" + RESPONSE_TYPE;
    }

}
