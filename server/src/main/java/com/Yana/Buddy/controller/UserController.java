package com.Yana.Buddy.controller;

import com.Yana.Buddy.dto.*;
import com.Yana.Buddy.entity.User;
import com.Yana.Buddy.service.TokenService;
import com.Yana.Buddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://yana-buddy.com, http://bucket-yana-buddy.s3-website.ap-northeast-2.amazonaws.com, https://accounts.google.com", allowedHeaders = "*", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

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

    @GetMapping("/oauth/google/callback")
    public ResponseEntity<TokenResponse> oauthLogin(String code) {
        String token = userService.oauthLogin(code);
        return new ResponseEntity(new TokenResponse(token, "bearer"), HttpStatus.OK);
    }

}
