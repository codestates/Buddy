package com.Yana.Buddy.service;

import com.Yana.Buddy.dto.GetEmailFromToken;
import com.Yana.Buddy.entity.User;
import com.Yana.Buddy.handler.ResponseEntityHandler;
import com.Yana.Buddy.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final ResponseEntityHandler responseHandler;
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String KEY;

    //JWT 토큰 생성
    public String createJwtToken(User user, long time) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("fresh")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofHours(time).toMillis()))
                .claim("email", user.getEmail())
                .claim("password", user.getPassword())
                .signWith(SignatureAlgorithm.HS256, KEY)
                .compact();
    }

    //JWT 토큰 유효성검사 및 토큰 claim 에 저장된 유저의 email 값 반환
    public GetEmailFromToken checkJwtToken(String key) {
        try {
            Claims claims = Jwts.parser().setSigningKey(KEY)
                    .parseClaimsJws(key)
                    .getBody();
            String email = (String) claims.get("email");

            return GetEmailFromToken.builder()
                    .email(email)
                    .message("토큰이 유효합니다.")
                    .build();
        } catch (ExpiredJwtException e) {
            return GetEmailFromToken.builder()
                    .email(null)
                    .message("토큰 시간이 만료되었습니다.")
                    .build();
        } catch (JwtException e) {
            return GetEmailFromToken.builder()
                    .email(null)
                    .message("토큰이 유효하지 않습니다.")
                    .build();
        }
    }

    //토큰 헤더 검증 및 Response Entity 반환
    public ResponseEntity<?> tokenCheckFromHeader(Map<String, String> header) {
        String authorization = header.get("authorization");
        if (authorization == null || !authorization.startsWith("Bearer")) {
            throw new IllegalArgumentException();
        } else {
            GetEmailFromToken checkResult = checkJwtToken(authorization.substring("Bearer ".length()));

            if (checkResult.getEmail() != null) {
                return responseHandler.userEntireInfo(userRepository.findByEmail(checkResult.getEmail()).get());
            } else {
                return responseHandler.badRequest(checkResult.getMessage());
            }
        }
    }

    /**
     1. request 의 쿠키들 중 refreshToken 이 담겨 있는 쿠키를 찾아냄
     2. 만약 refreshToken 쿠키가 없다면 bad request 반환
     3. refreshToken 쿠키가 있다면 해당 쿠키 안의 JWT 토큰을 파싱해서 유저 정보 찾기
     4. JWT 토큰을 생성해줌과 동시에 200 OK 반환
     */
    public ResponseEntity renewalToken(HttpServletRequest request) {
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

        GetEmailFromToken checkResult = checkJwtToken(cookieResult);
        if (checkResult.getEmail() != null) {
            User user = userRepository.findByEmail(checkResult.getEmail()).get();
            String finalCookiesResult = cookieResult;
            return responseHandler.singleSuccessResponse(createJwtToken(user, 1L));
        } else {
            return responseHandler.badRequest(checkResult.getMessage());
        }
    }

}
