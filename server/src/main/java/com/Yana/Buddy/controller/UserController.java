package com.Yana.Buddy.controller;

import com.Yana.Buddy.dto.*;
import com.Yana.Buddy.entity.User;
import com.Yana.Buddy.handler.ResponseEntityHandler;
import com.Yana.Buddy.service.OAuthService;
import com.Yana.Buddy.service.TokenService;
import com.Yana.Buddy.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final ResponseEntityHandler responseHandler;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto, HttpServletResponse response) {
        try {
            User user = userService.findUserByEmail(dto.getEmail());
            if (userService.passwordCheck(user, dto.getPassword())) {
                String accessToken = tokenService.createJwtToken(user, 1L);
                String refreshToken = tokenService.createJwtToken(user, 2L);
                Cookie cookie = new Cookie("refreshToken", refreshToken);
                response.addCookie(cookie);

                return responseHandler.loginSuccess(
                        new LoginSuccessResponse(user.getId(), user.getEmail(), user.getNickname(),
                                accessToken, refreshToken, "기본 로그인에 성공했습니다.")
                );
            } else {
                return responseHandler.badRequest("비밀번호가 틀렸습니다.");
            }
        } catch (Exception e) {
            return responseHandler.badRequest("email 혹은 비밀번호가 올바르지 않습니다.");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterDto dto) {
        try {
            User user = userService.join(dto);
            return responseHandler.userBasicInfo(new UserBasicInfoResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getNickname(),
                    user.getGender().getValue(),
                    user.getStateMessage(),
                    user.getProfileImage(),
                    "회원 가입에 성공했습니다."
            ));
        } catch (Exception e) {
            return responseHandler.badRequest("회원 가입에 실패했습니다.");
        }
    }

    //Email 중복 체크
    @PostMapping("/email_check")
    public ResponseEntity<?> emailCheck(@RequestBody EmailDto dto) {
        if (userService.existEmail(dto.getEmail())) {
            return responseHandler.badRequest("이미 존재하는 이메일입니다!");
        } else {
            return responseHandler.singleSuccessResponse("사용 가능한 이메일입니다.");
        }
    }

    //Nickname 중복 체크
    @PostMapping("/nickname_check")
    public ResponseEntity<?> nicknameCheck(@RequestBody NicknameDto dto) {
        if (userService.existNickname(dto.getNickname())) {
            return responseHandler.badRequest("이미 존재하는 닉네임입니다.");
        } else {
            return responseHandler.singleSuccessResponse("사용 가능한 닉네임입니다.");
        }
    }

    //Access Token 유효성 검사
    @GetMapping("/token_check")
    public ResponseEntity<?> tokenValidCheck(@RequestHeader Map<String, String> header) {
        tokenService.isValidAuthHeader(header.get("authorization"));
        String key = tokenService.extractToken(header.get("authorization"));
        Map<String, String> checkResult = tokenService.checkJwtToken(key);

        if (checkResult.get("email") != null) {
            return responseHandler.userEntireInfo(userService.findUserByEmail(checkResult.get("email")));
        } else {
            return responseHandler.badRequest(checkResult.get("message"));
        }
    }

    //Access Token이 유효하지 않을 경우, Token 재발급
    @GetMapping("/renewal_token")
    public ResponseEntity<?> renewalToken(HttpServletRequest request) {
        String cookieResult = "";
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refreshToken")) {
                cookieResult = cookie.getValue();
            }
        }

        if (cookieResult.equals("")) {
            return responseHandler.badRequest("Refresh Token 이 존재하지 않습니다.");
        }

        Map<String, String> checkResult = tokenService.checkJwtToken(cookieResult);
        if (checkResult.get("email") != null) {
            User user = userService.findUserByEmail(checkResult.get("email"));
            String finalCookiesResult = cookieResult;
            return responseHandler.singleSuccessResponse(tokenService.createJwtToken(user, 1L));
        } else {
            return responseHandler.badRequest(checkResult.get("message"));
        }
    }

    //유저 정보 조회
    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable("id") Long id) {
        try {
            User user = userService.findUserById(id);

            return responseHandler.userBasicInfo(new UserBasicInfoResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getNickname(),
                    user.getGender().getValue(),
                    user.getStateMessage(),
                    user.getProfileImage(),
                    "유저 정보가 성공적으로 조회되었습니다."
            ));
        } catch (Exception e) {
            return responseHandler.badRequest("유저 정보 조회에 실패했습니다.");
        }
    }

    //유저 정보 수정
    @PutMapping("/profile/{id}")
    public ResponseEntity<?> editProfile(@PathVariable("id") Long id, @RequestBody EditProfileDto dto) {
        try {
            User user = userService.editProfile(id, dto);
            return responseHandler.userBasicInfo(new UserBasicInfoResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getNickname(),
                    user.getGender().getValue(),
                    user.getStateMessage(),
                    user.getProfileImage(),
                    "유저 정보가 수정되었습니다."
            ));
        } catch (Exception e) {
            return responseHandler.badRequest("회원 정보 수정에 실패했습니다.");
        }
    }

    //유저 삭제
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.deleteUser(id);
            return responseHandler.singleSuccessResponse("유저 정보가 삭제되었습니다.");
        } catch (Exception e) {
            return responseHandler.badRequest("유저 정보 삭제에 실패했습니다.");
        }
    }

    //Google Login API 접속 후 생성되는 code 를 이 API 에 전송
    @GetMapping("/oauth/google/callback")
    public ResponseEntity<?> googleOAuthLogin(String code, HttpServletResponse response) {
        return responseHandler.loginSuccess(userService.googleLogin(code, response));
    }

    //Google 과 똑같은 로직
    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<?> kakaoOAuthLogin(String code, HttpServletResponse response) throws JsonProcessingException, ParseException {
        return responseHandler.loginSuccess(userService.kakaoLogin(code, response));
    }

}
