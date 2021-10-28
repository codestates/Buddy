package com.Yana.Buddy.controller;

import com.Yana.Buddy.dto.*;
import com.Yana.Buddy.entity.User;
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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(
        origins = "https://yana-buddy.com, " +
                "http://bucket-yana-buddy.s3-website.ap-northeast-2.amazonaws.com, " +
                "https://accounts.google.com, https://www.googleapis.com, " +
                "https://kauth.kakao.com",
        allowedHeaders = "*",
        allowCredentials = "true"
)
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;
    private final OAuthService oAuthService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto, HttpServletResponse response) {
        try {
            User user = userService.findUserByEmail(dto.getEmail());
            if (userService.passwordCheck(user, dto.getPassword())) {
                String accessToken = tokenService.createJwtToken(user, 1L);
                String refreshToken = tokenService.createJwtToken(user, 2L);
                Cookie cookie = new Cookie("refreshToken", refreshToken);
                response.addCookie(cookie);

                return ResponseEntity.status(200).body(new HashMap<>() {
                    {
                        put("id", user.getId());
                        put("accessToken", accessToken);
                        put("refreshToken", refreshToken);
                        put("message", "로그인에 성공했습니다.");
                    }
                });
            } else {
                return ResponseEntity.status(400).body(new HashMap<>() {
                    {
                        put("message", "비밀번호가 틀렸습니다.");
                    }
                });
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new HashMap<>() {
                {
                    put("message", "Email이나 비밀번호 입력이 올바르지 않습니다.");
                }
            });
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterDto dto) {
        try {
            User user = userService.join(dto);
            return ResponseEntity.status(200).body(new HashMap<>() {
                {
                    put("id", user.getId());
                    put("email", user.getEmail());
                    put("nickname", user.getNickname());
                    put("gender", user.getGender());
                    put("message", "회원가입에 성공했습니다.");
                }
            });
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new HashMap<>() {
                {
                    put("message", "회원가입에 실패했습니다.");
                }
            });
        }
    }

    //Email 중복 체크
    @PostMapping("/email_check")
    public ResponseEntity<?> emailCheck(@RequestBody EmailDto dto) {
        if (userService.existEmail(dto.getEmail())) {
            return ResponseEntity.status(400).body(new HashMap<>() {
                {
                    put("message", "이미 존재하는 이메일입니다!");
                }
            });
        } else {
            return ResponseEntity.status(200).body(new HashMap<>() {
                {
                    put("message", "사용 가능한 이메일입니다.");
                }
            });
        }
    }

    //Nickname 중복 체크
    @PostMapping("/nickname_check")
    public ResponseEntity<?> nicknameCheck(@RequestBody NicknameDto dto) {
        if (userService.existNickname(dto.getNickname())) {
            return ResponseEntity.status(400).body(new HashMap<>() {
                {
                    put("message", "이미 존재하는 닉네임입니다.");
                }
            });
        } else {
            return ResponseEntity.status(200).body(new HashMap<>() {
                {
                    put("message", "사용 가능한 닉네임입니다.");
                }
            });
        }
    }

    //Access Token 유효성 검사
    @GetMapping("/token_check")
    public ResponseEntity<?> tokenValidCheck(@RequestHeader Map<String, String> header) {
        tokenService.isValidAuthHeader(header.get("authorization"));
        String key = tokenService.extractToken(header.get("authorization"));

        Map<String, String> checkResult = tokenService.checkJwtToken(key);
        if (checkResult.get("email") != null) {
            User user = userService.findUserByEmail(checkResult.get("email"));

            return ResponseEntity.status(200).body(new HashMap<>() {
                {
                    put("message", checkResult.get("message"));
                    put("id", user.getId());
                    put("email", user.getEmail());
                    put("nickname", user.getNickname());
                    put("gender", user.getGender());
                    put("authority", user.getAuthority());
                    put("profile_image", user.getProfileImage());
                    put("state_message", user.getStateMessage());
                    put("password", user.getPassword());
                }
            });
        } else {
            return ResponseEntity.status(400).body(new HashMap<>() {
                {
                    put("message", checkResult.get("message"));
                }
            });
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
            return ResponseEntity.status(400).body(new HashMap<>() {
                {
                    put("data", null);
                    put("message", "refresh token 이 존재하지 않습니다.");
                }
            });
        }

        Map<String, String> checkResult = tokenService.checkJwtToken(cookieResult);
        if (checkResult.get("email") != null) {
            User user = userService.findUserByEmail(checkResult.get("email"));
            String finalCookiesResult = cookieResult;
            return ResponseEntity.status(200).body(new HashMap<>() {
                {
                    put("data", new HashMap<>() {
                        {
                            put("accessToken", tokenService.createJwtToken(user, 1L));
                        }
                    });
                }
            });
        } else {
            return ResponseEntity.status(400).body(new HashMap<>() {
                {
                    put("data", null);
                    put("message", checkResult.get("message"));
                }
            });
        }
    }

    //유저 정보 조회
    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable("id") Long id) {
        try {
            User user = userService.findUserById(id);

            return ResponseEntity.status(200).body(new HashMap<>() {
                {
                    put("message", "유저 정보가 성공적으로 조회되었습니다.");
                    put("id", user.getId());
                    put("email", user.getEmail());
                    put("nickname", user.getNickname());
                    put("gender", user.getGender());
                    put("state_message", user.getStateMessage());
                    put("profile_image", user.getProfileImage());
                }
            });
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new HashMap<>() {
                {
                    put("message", "유저 정보 조회에 실패했습니다.");
                }
            });
        }
    }

    //유저 정보 수정
    @PutMapping("/profile/{id}")
    public ResponseEntity<?> editProfile(@PathVariable("id") Long id, @RequestBody EditProfileDto dto) {
        try {
            User user = userService.editProfile(id, dto);
            return ResponseEntity.status(200).body(new HashMap<>() {
                {
                    put("message", "유저 정보가 수정되었습니다.");
                    put("id", user.getId());
                    put("email", user.getEmail());
                    put("nickname", user.getNickname());
                    put("state_message", user.getStateMessage());
                    put("profile_image", user.getProfileImage());
                }
            });
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new HashMap<>() {
                {
                    put("message", "회원 정보 수정에 실패했습니다.");
                }
            });
        }
    }

    //유저 삭제
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.status(200).body(new HashMap<>() {
                {
                    put("message", "유저 정보가 삭제되었습니다.");
                    put("id", id);
                }
            });
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new HashMap<>() {
                {
                    put("message", "유저 정보 삭제에 실패했습니다.");
                }
            });
        }
    }

    /*
    Google Login API 접속 후 생성되는 code를 이 API에 전송
    code를 받아와서 Google의 관련 API를 이용하여 구글 토큰을 받아옴
    해당 구글 토큰을 통해 구글 유저 정보를 가져옴
    유저의 이메일이 우리 서비스에 이미 가입된 계정이라면 회원 가입 진행
    이후, 해당 이메일 계정 로그인 진행 및 토큰 생성
    유저 정보 반환
     */
    @GetMapping("/oauth/google/callback")
    public ResponseEntity<?> googleOAuthLogin(String code, HttpServletResponse response) {
        OAuthLoginDto googleLoginUser = userService.googleOAuthLogin(code);
        response.addCookie(googleLoginUser.getCookie());
        return ResponseEntity.status(200).body(new HashMap<>() {
            {
                put("id", googleLoginUser.getUser().getId());
                put("email", googleLoginUser.getUser().getEmail());
                put("nickname", googleLoginUser.getUser().getNickname());
                put("accessToken", googleLoginUser.getAccessToken());
                put("refreshToken", googleLoginUser.getRefreshToken());
                put("message", "Google Login 에 성공했습니다!");
            }
        });
    }

    /*
    Google과 똑같은 로직으로 code를 통해 토큰을 받아오고,
    토큰을 통해 유저 정보를 가져오고,
    유저 정보 안의 이메일이 우리 서비스에 가입되어 있지 않다면 회원 가입 진행 후,
    해당 유저 계정으로 로그인까지 진행
    유저 정보 반환
     */
    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<?> kakaoOAuthLogin(String code, HttpServletResponse response) throws JsonProcessingException, ParseException {
        OAuthLoginDto kakaoLoginUser = userService.kakaoOAuthLogin(code);
        response.addCookie(kakaoLoginUser.getCookie());
        return ResponseEntity.status(200).body(new HashMap<>() {
            {
                put("id", kakaoLoginUser.getUser().getId());
                put("email", kakaoLoginUser.getUser().getEmail());
                put("nickname", kakaoLoginUser.getUser().getNickname());
                put("accessToken", kakaoLoginUser.getAccessToken());
                put("refreshToken", kakaoLoginUser.getRefreshToken());
                put("message", "Kakao Login 에 성공했습니다!");
            }
        });
    }

}
