package com.Yana.Buddy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OAuthLoginController {

    private static final String REDIRECT_URI = "http://bucket-yana-buddy.s3-website.ap-northeast-2.amazonaws.com";
    private static final String RESPONSE_TYPE = "code";
    private static final String ACCESS_TYPE = "offline";
    private static final String SCOPE = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";

    @Value("${oauth.google.client-id}") private String G_CLIENT_ID;
    @Value("${oauth.kakao.client-id}") private String K_CLIENT_ID;

    private String ENDPOINT = "";

    //redirect를 통한 Google Login API 접속
    @GetMapping("/login_google")
    public String GoogleLogin() {
        ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
        return "redirect:" + ENDPOINT + "?client_id=" + G_CLIENT_ID + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=" + RESPONSE_TYPE + "&scope=" + SCOPE + "&access_type=" + ACCESS_TYPE;
    }

    //redirect를 통한 Kakao Login API 접속
    @GetMapping("/login_kakao")
    public String KakaoLogin() {
        ENDPOINT = "https://kauth.kakao.com/oauth/authorize";
        return "redirect:" + ENDPOINT + "?client_id=" + K_CLIENT_ID + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=" + RESPONSE_TYPE;
    }

}
