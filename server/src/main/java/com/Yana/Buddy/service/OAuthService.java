package com.Yana.Buddy.service;

import com.Yana.Buddy.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
        origins = "https://yana-buddy.com, " +
                "http://bucket-yana-buddy.s3-website.ap-northeast-2.amazonaws.com, " +
                "https://accounts.google.com, https://www.googleapis.com, " +
                "https://kauth.kakao.com",
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

    //구글에서 받은 code를 통해 token 받아오기 (JSON 타입으로 반환되기에, ResponseEntity로 묶어줌)
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

    //JSON 타입의 token 정보를 dto 클래스를 활용하여 매핑 및 반환
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

    //구글의 access token을 활용하여 구글 유저 정보 받아오기(JSON 타입을 ResponseEntity로 묶어줌)
    public ResponseEntity<String> createGetRequest(GoogleToken googleToken) {
        String url = "https://www.googleapis.com/oauth2/v1/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleToken.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    }

    //JSON 타입으로 전달받은 유저 정보를 dto 클래스를 활용하여 매핑 및 반환
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

    //카카오에서 받은 code를 통해 token 받아오기 (JSON 타입으로 반환되기에, ResponseEntity로 묶어줌)
    public ResponseEntity<String> getTokenInfo(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", K_CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);
        params.add("client_secret", K_CLIENT_SECRET);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
    }

    //JSON 타입의 token 정보를 dto 클래스를 활용하여 매핑 및 반환
    public KakaoToken getKakaoToken(ResponseEntity<String> response) {
        KakaoToken kakaoToken = null;
        try {
            kakaoToken = objectMapper.readValue(response.getBody(), KakaoToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoToken;
    }

    /*
    카카오의 access token을 활용하여 카카오 유저 정보 받아옴
    카카오의 유저 정보는 JSON 형태가 다소 복잡하기 때문에 JSONParser를 활용하여 필요한 정보들만 가져온 뒤,
    카카오 전용 회원가입 dto를 통해 전달
     */
    public KakaoRegisterDto getKakaoUser(KakaoToken kakaoToken) throws JsonProcessingException, ParseException {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoToken.getAccess_token());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<String> result =  restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        String jsonValue = objectMapper.writeValueAsString(result);

        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(jsonValue);
        String body = (String) object.get("body");
        JSONObject main = (JSONObject) parser.parse(body);

        JSONObject kakao_account = (JSONObject) main.get("kakao_account");

        String email = kakao_account.get("email").toString();

        JSONObject profile = (JSONObject) kakao_account.get("profile");
        String nickname = profile.get("nickname").toString();
        String profile_image = profile.get("profile_image_url").toString();
        String gender = kakao_account.get("gender").toString().toUpperCase();

        return new KakaoRegisterDto(email, nickname, profile_image, gender);
    }

}