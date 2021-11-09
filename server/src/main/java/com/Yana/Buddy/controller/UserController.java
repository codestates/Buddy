package com.Yana.Buddy.controller;

import com.Yana.Buddy.dto.*;
import com.Yana.Buddy.handler.ResponseEntityHandler;
import com.Yana.Buddy.service.TokenService;
import com.Yana.Buddy.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final ResponseEntityHandler responseHandler;

    //기본 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        return userService.basicLogin(dto);
    }

    //기본 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterDto dto) {
        return userService.basicSignup(dto);
    }

    //Email 중복 체크
    @PostMapping("/email_check")
    public ResponseEntity emailCheck(@RequestBody EmailDto dto) {
        return userService.emailCheck(dto);
    }

    //Nickname 중복 체크
    @PostMapping("/nickname_check")
    public ResponseEntity nicknameCheck(@RequestBody NicknameDto dto) {
        return userService.nicknameCheck(dto);
    }

    //Access Token 유효성 검사
    @GetMapping("/token_check")
    public ResponseEntity<?> tokenValidCheck(@RequestHeader Map<String, String> header) {
        return tokenService.tokenCheckFromHeader(header);
    }

    //Access Token 이 유효하지 않을 경우 호출하는 API -> Token 재발급 용도
    @PostMapping("/renewal_token")
    public ResponseEntity<?> renewalToken(@RequestBody RefreshTokenDto dto) {
        return tokenService.renewalToken(dto);
    }

    //유저 정보 조회
    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable("id") Long id) {
        return userService.getUserInfo(id);
    }

    //유저 정보 수정
    @PutMapping("/profile/{id}")
    public ResponseEntity<?> editProfile(@PathVariable("id") Long id, @RequestBody EditProfileDto dto) {
        return userService.editProfile(id, dto);
    }

    //유저 삭제
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        return userService.deleteUser(id);
    }

    //Google Login API 접속 후 생성되는 code 를 이 API 에 전송
    @GetMapping("/oauth/google/callback")
    public ResponseEntity<?> googleOAuthLogin(String code) {
        return responseHandler.loginSuccess(userService.googleLogin(code));
    }

    //Google 과 똑같은 로직
    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<?> kakaoOAuthLogin(String code) throws JsonProcessingException, ParseException {
        return responseHandler.loginSuccess(userService.kakaoLogin(code));
    }

}
