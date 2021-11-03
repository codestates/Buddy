package com.Yana.Buddy.service;

import com.Yana.Buddy.dto.*;
import com.Yana.Buddy.entity.Gender;
import com.Yana.Buddy.entity.Role;
import com.Yana.Buddy.entity.User;
import com.Yana.Buddy.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final OAuthService oAuthService;
    private final TokenService tokenService;

    //로그인 시 입력한 비밀번호가 맞는지 검증
    public boolean passwordCheck(User user, String password) {
        if (user.getPassword().equals(password)) return true;
        else return false;
    }

    //회원 가입
    public User join(RegisterDto dto) {
        User user = User.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .profileImage("https://buddy-image-server.s3.ap-northeast-2.amazonaws.com/mypage_img.png")
                .gender(Gender.valueOf(dto.getGender()))
                .authority(Role.GENERAL)
                .build();
        userRepository.save(user);
        return user;
    }

    //Email 중복 체크
    public boolean existEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    //Nickname 중복 체크
    public boolean existNickname(String nickname) {
        Optional<User> user = userRepository.findByNickname(nickname);
        if (user.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).get();
    }

    //유저 정보 수정
    public User editProfile(Long id, EditProfileDto dto) {
        User userRepo = userRepository.findById(id).get();
        User user = User.builder()
                .id(id)
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .email(userRepo.getEmail())
                .gender(userRepo.getGender())
                .authority(Role.GENERAL)
                .stateMessage(dto.getStateMessage())
                .profileImage(dto.getProfile_image())
                .build();
        userRepository.save(user);
        return user;
    }

    //유저 삭제
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
    Google로부터 받은 code를 받아와서 Google의 관련 API를 이용하여 구글 토큰을 받아옴
    해당 구글 토큰을 통해 구글 유저 정보를 가져옴
    유저의 이메일이 우리 서비스에 이미 가입된 계정이라면 회원 가입 진행
    이후, 해당 이메일 계정 로그인 진행 및 토큰 생성
    유저 정보 반환
     */
    public OAuthLoginDto googleOAuthLogin(String code) {
        ResponseEntity<String> accessTokenResponse = oAuthService.createPostRequest(code);
        GoogleToken googleToken = oAuthService.getAccessToken(accessTokenResponse);

        ResponseEntity<String> userInfoResponse = oAuthService.createGetRequest(googleToken);
        GoogleUser googleUser = oAuthService.getUserInfo(userInfoResponse);

        if (!existEmail(googleUser.getEmail())) {
            googleSignUp(googleUser, googleToken);
        }

        User user = userRepository.findByEmail(googleUser.getEmail()).orElseThrow();
        String accessToken = tokenService.createJwtToken(user, 1L);
        String refreshToken = tokenService.createJwtToken(user, 2L);
        Cookie cookie = new Cookie("refreshToken", refreshToken);

        return new OAuthLoginDto(user, accessToken, refreshToken, cookie);
    }

    /**
    Google과 똑같은 로직으로 code를 통해 토큰을 받아오고,
    토큰을 통해 유저 정보를 가져오고,
    유저 정보 안의 이메일이 우리 서비스에 가입되어 있지 않다면 회원 가입 진행 후,
    해당 유저 계정으로 로그인까지 진행
    유저 정보 반환
     */
    public OAuthLoginDto kakaoOAuthLogin(String code) throws JsonProcessingException, ParseException {
        ResponseEntity<String> kakaoTokenResponse = oAuthService.getTokenInfo(code);
        KakaoToken kakaoToken = oAuthService.getKakaoToken(kakaoTokenResponse);

        KakaoRegisterDto kakaoUserInfo = oAuthService.getKakaoUser(kakaoToken);

        if (!existEmail(kakaoUserInfo.getEmail())) {
            kakaoSignUp(kakaoUserInfo);
        }

        User user = userRepository.findByEmail(kakaoUserInfo.getEmail()).orElseThrow();
        String accessToken = tokenService.createJwtToken(user, 1L);
        String refreshToken = tokenService.createJwtToken(user, 2L);
        Cookie cookie = new Cookie("refreshToken", refreshToken);

        return new OAuthLoginDto(user, accessToken, refreshToken, cookie);
    }

    //Google 전용 회원 가입 로직
    private void googleSignUp(GoogleUser user, GoogleToken googleToken) {
        User signUpUser = User.builder()
                .email(user.getEmail())
                .nickname(user.name)
                .profileImage(user.getPicture())
                .authority(Role.GENERAL)
                .build();

        userRepository.save(signUpUser);
    }

    //Kakao 전용 회원 가입 로직
    public User kakaoSignUp(KakaoRegisterDto dto) {
        Gender gender = null;
        if (dto.getGender().equals("MALE")) {
            gender = Gender.MALE;
        } else if (dto.getGender().equals("FEMALE")) {
            gender = Gender.FEMALE;
        }

        User signUpUser = User.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .profileImage(dto.getProfile_image())
                .gender(gender)
                .authority(Role.GENERAL)
                .build();

        userRepository.save(signUpUser);
        return signUpUser;
    }

    //Google Login 에 대한 return 값 설정
    public LoginSuccessResponse googleLogin(String code, HttpServletResponse response) {
        OAuthLoginDto googleLoginUser = googleOAuthLogin(code);
        response.addCookie(googleLoginUser.getCookie());

        return new LoginSuccessResponse(
                googleLoginUser.getUser().getId(),
                googleLoginUser.getUser().getEmail(),
                googleLoginUser.getUser().getNickname(),
                googleLoginUser.getAccessToken(),
                googleLoginUser.getRefreshToken(),
                "Google Login 에 성공했습니다.");
    }

    //Kakao Login 에 대한 return 값 설정
    public LoginSuccessResponse kakaoLogin(String code, HttpServletResponse response) throws ParseException, JsonProcessingException {
        OAuthLoginDto kakaoLoginUser = kakaoOAuthLogin(code);
        response.addCookie(kakaoLoginUser.getCookie());

        return new LoginSuccessResponse(
                kakaoLoginUser.getUser().getId(),
                kakaoLoginUser.getUser().getEmail(),
                kakaoLoginUser.getUser().getNickname(),
                kakaoLoginUser.getAccessToken(),
                kakaoLoginUser.getRefreshToken(),
                "Kakao Login 에 성공했습니다.");
    }

}
