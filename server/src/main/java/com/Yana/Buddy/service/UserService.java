package com.Yana.Buddy.service;

import com.Yana.Buddy.controller.UserController;
import com.Yana.Buddy.dto.*;
import com.Yana.Buddy.entity.Gender;
import com.Yana.Buddy.entity.Role;
import com.Yana.Buddy.entity.User;
import com.Yana.Buddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
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

    public String oauthLogin(String code) {
        ResponseEntity<String> accessTokenResponse = oAuthService.createPostRequest(code);
        log.info("token response 확인 : " + accessTokenResponse.toString());
        OAuthToken oAuthToken = oAuthService.getAccessToken(accessTokenResponse);

        log.info("access token 확인 : " + oAuthToken.getAccess_token());

        ResponseEntity<String> userInfoResponse = oAuthService.createGetRequest(oAuthToken);
        GoogleUser googleUser = oAuthService.getUserInfo(userInfoResponse);

        if (!isJoinedUser(googleUser)) {
            googleSignUp(googleUser, oAuthToken);
        }

        User user = userRepository.findByEmail(googleUser.getEmail()).orElseThrow();

        return tokenService.createJwtToken(user, 1L);
    }

    private boolean isJoinedUser(GoogleUser googleUser) {
        Optional<User> searchUser = userRepository.findByEmail(googleUser.getEmail());
        return searchUser.isPresent();
    }

    private void googleSignUp(GoogleUser user, OAuthToken oAuthToken) {
        User signUpUser = User.builder()
                .email(user.getEmail())
                .profileImage(user.getPicture())
                .authority(Role.GENERAL)
                .build();

        userRepository.save(signUpUser);
    }

}
