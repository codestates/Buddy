package com.Yana.Buddy.service;

import com.Yana.Buddy.dto.EditProfileDto;
import com.Yana.Buddy.dto.OAuthToken;
import com.Yana.Buddy.dto.RegisterDto;
import com.Yana.Buddy.entity.Gender;
import com.Yana.Buddy.entity.Role;
import com.Yana.Buddy.entity.User;
import com.Yana.Buddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;

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
        OAuthToken oAuthToken = oAuthService.getAccessToken(accessTokenResponse);

        ResponseEntity<String> userInfoResponse = oAuthService.createGetRequest(oAuthToken);
        User googleUser = oAuthService.getUserInfo(userInfoResponse);

        if (!isJoinedUser(googleUser)) {
            googleSignUp(googleUser, oAuthToken);
        }

        User user = userRepository.findByEmail(googleUser.getEmail()).orElseThrow();

        return tokenService.createJwtToken(user, 1L);
    }

    private boolean isJoinedUser(User user) {
        Optional<User> searchUser = userRepository.findByEmail(user.getEmail());
        return searchUser.isPresent();
    }

    private void googleSignUp(User user, OAuthToken oAuthToken) {
        User signUpUser = User.builder()
                .email(user.getEmail())
                .profileImage(user.getProfileImage())
                .build();

        userRepository.save(signUpUser);
    }

}
