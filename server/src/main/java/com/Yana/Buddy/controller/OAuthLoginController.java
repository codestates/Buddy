package com.Yana.Buddy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OAuthLoginController {

    private final String REDIRECT_URI = "http://bucket-yana-buddy.s3-website.ap-northeast-2.amazonaws.com";
    private final String RESPONSE_TYPE = "code";
    private final String ACCESS_TYPE = "offline";
    private final String SCOPE = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
    private final String G_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
    private final String K_ENDPOINT = "https://kauth.kakao.com/oauth/authorize";

    @Value("${oauth.google.client-id}") private String G_CLIENT_ID;
    @Value("${oauth.kakao.client-id}") private String K_CLIENT_ID;

    //redirect 를 통한 Google Login API 접속
    @GetMapping("/login_google")
    public String GoogleLogin() {
        return "redirect:" + G_ENDPOINT + "?client_id=" + G_CLIENT_ID + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=" + RESPONSE_TYPE + "&scope=" + SCOPE + "&access_type=" + ACCESS_TYPE;
    }

    //redirect 를 통한 Kakao Login API 접속
    @GetMapping("/login_kakao")
    public String KakaoLogin() {
        return "redirect:" + K_ENDPOINT + "?client_id=" + K_CLIENT_ID + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=" + RESPONSE_TYPE;
    }

}
