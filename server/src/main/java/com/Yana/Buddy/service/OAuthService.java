package com.Yana.Buddy.service;

import com.Yana.Buddy.dto.GoogleUser;
import com.Yana.Buddy.dto.GoogleToken;
import com.Yana.Buddy.dto.KakaoToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        allowCredentials = "true"
)
public class OAuthService {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    private static final String REDIRECT_URI = "http://bucket-yana-buddy.s3-website.ap-northeast-2.amazonaws.com";
    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.google.client-id}") private String G_CLIENT_ID;
    @Value("${oauth.google.client-secret}") private String G_CLIENT_SECRET;

    @Value("${oauth.kakao.client-id}") private String K_CLIENT_ID;
    @Value("${oauth.kakao.client-secret}") private String K_CLIENT_SECRET;

    public ResponseEntity<String> createPostRequest(String code) {
        String url = "https://oauth2.googleapis.com/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", G_CLIENT_ID);
        params.add("client_secret", G_CLIENT_SECRET);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("grant_type", GRANT_TYPE);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
    }

    public GoogleToken getAccessToken(ResponseEntity<String> response) {
        GoogleToken googleToken = null;
        try {
            googleToken = objectMapper.readValue(response.getBody(), GoogleToken.class);
        } catch (JsonMappingException e) {
            log.error("get access token Exception : " + e);
        } catch (JsonProcessingException e) {
            log.error("get access token Exception : " + e);
        }

        return googleToken;
    }

    public ResponseEntity<String> createGetRequest(GoogleToken googleToken) {
        String url = "https://www.googleapis.com/oauth2/v1/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleToken.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    }

    public GoogleUser getUserInfo(ResponseEntity<String> response) {
        GoogleUser user = null;
        try {
            user = objectMapper.readValue(response.getBody(), GoogleUser.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return user;
    }

    public ResponseEntity<KakaoToken> getKakaoToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", GRANT_TYPE);
        params.add("client_id", K_CLIENT_ID);
        params.add("client_secret", K_CLIENT_SECRET);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, KakaoToken.class);
    }

}
