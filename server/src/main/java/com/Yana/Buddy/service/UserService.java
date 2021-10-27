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

    public boolean passwordCheck(User user, String password) {
        if (user.getPassword().equals(password)) return true;
        else return false;
    }

    public User join(RegisterDto dto) {
        User user = User.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .gender(Gender.valueOf(dto.getGender()))
                .authority(Role.GENERAL)
                .build();
        userRepository.save(user);
        return user;
    }

    public boolean existEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

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
                .build();
        userRepository.save(user);
        return user;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public OAuthLoginDto googleOAuthLogin(String code) {
        ResponseEntity<String> accessTokenResponse = oAuthService.createPostRequest(code);
        GoogleToken googleToken = oAuthService.getAccessToken(accessTokenResponse);

        ResponseEntity<String> userInfoResponse = oAuthService.createGetRequest(googleToken);
        GoogleUser googleUser = oAuthService.getUserInfo(userInfoResponse);

        if (!isJoinedUser(googleUser)) {
            googleSignUp(googleUser, googleToken);
        }

        User user = userRepository.findByEmail(googleUser.getEmail()).orElseThrow();
        String accessToken = tokenService.createJwtToken(user, 1L);
        String refreshToken = tokenService.createJwtToken(user, 2L);
        Cookie cookie = new Cookie("refreshToken", refreshToken);

        return new OAuthLoginDto(user, accessToken, refreshToken, cookie);
    }

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

    private boolean isJoinedUser(GoogleUser googleUser) {
        Optional<User> searchUser = userRepository.findByEmail(googleUser.getEmail());
        return searchUser.isPresent();
    }

    private void googleSignUp(GoogleUser user, GoogleToken googleToken) {
        User signUpUser = User.builder()
                .email(user.getEmail())
                .nickname(user.name)
                .profileImage(user.getPicture())
                .authority(Role.GENERAL)
                .build();

        userRepository.save(signUpUser);
    }

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

}
