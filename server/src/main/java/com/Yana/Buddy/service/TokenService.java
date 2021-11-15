package com.Yana.Buddy.service;

import com.Yana.Buddy.dto.GetEmailFromToken;
import com.Yana.Buddy.dto.RefreshTokenDto;
import com.Yana.Buddy.entity.User;
import com.Yana.Buddy.handler.ResponseEntityHandler;
import com.Yana.Buddy.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final ResponseEntityHandler responseHandler;
    private final UserRepository userRepository;
    private final RedisTemplate redisTemplate;

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
     1. 전달받은 email 값을 key 값으로 하는 기존의 저장된 refresh token 과, 전달받은 refresh token 이 같은지 검증
     2. 전달받은 email 을 가진 유저가 아직 존재하는지 검증
     3. 위 두 조건을 모두 충족한다면 access token 발급 및 반환
     */
    public ResponseEntity renewalToken(RefreshTokenDto dto) {
        User user = null;

        if (!dto.getRefreshToken().equals(redisTemplate.opsForValue().get(dto.getEmail()))) {
            return responseHandler.badRequest("로그인 시 지정된 Refresh Token 과 값이 일치하지 않습니다.");
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            user = userRepository.findByEmail(dto.getEmail()).get();
            return responseHandler.singleSuccessResponse(createJwtToken(user, 1L));
        } else {
            return responseHandler.badRequest("해당 유저 정보는 더이상 존재하지 않습니다.");
        }
    }

}
