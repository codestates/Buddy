package com.Yana.Buddy.service;

import com.Yana.Buddy.dto.*;
import com.Yana.Buddy.entity.Gender;
import com.Yana.Buddy.entity.Role;
import com.Yana.Buddy.entity.User;
import com.Yana.Buddy.handler.ResponseEntityHandler;
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
    private final ResponseEntityHandler responseHandler;

    //기본 로그인 (소셜 로그인 X)
    public ResponseEntity<?> basicLogin(LoginDto dto, HttpServletResponse response) {
        if (findUserByEmail(dto.getEmail()).isPresent()) {
            User user = findUserByEmail(dto.getEmail()).get();
            if (passwordCheck(user, dto.getPassword())) {
                String accessToken = tokenService.createJwtToken(user, 1L);
                String refreshToken = tokenService.createJwtToken(user, 2L);
                Cookie cookie = new Cookie("refreshToken", refreshToken);
                response.addCookie(cookie);

                return responseHandler.loginSuccess(
                        new LoginSuccessResponse(user.getId(), user.getEmail(), user.getNickname(),
                                accessToken, refreshToken, "기본 로그인에 성공했습니다.")
                );
            } else {
                return responseHandler.badRequest("비밀번호가 일치하지 않습니다.");
            }
        } else {
            return responseHandler.badRequest("해당 Email 은 등록되지 않았습니다.");
        }
    }

    //기본 회원가입 (소셜 회원가입 X)
    public ResponseEntity<?> basicSignup(RegisterDto dto) {
        User user = User.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .password(dto.getPassword())
                .profileImage("https://buddy-image-server.s3.ap-northeast-2.amazonaws.com/mypage_img.png")
                .gender(Gender.valueOf(dto.getGender()))
                .authority(Role.GENERAL)
                .build();

        userRepository.save(user);

        return responseHandler.userBasicInfo(UserBasicInfoResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .nickname(user.getNickname())
                        .gender(user.getGender().getValue())
                        .stateMessage(user.getStateMessage())
                        .profileImage(user.getProfileImage())
                        .message("회원 가입에 성공했습니다.")
                        .build());
    }

    //로그인 시 입력한 비밀번호가 맞는지 검증
    public boolean passwordCheck(User user, String password) {
        if (user.getPassword().equals(password)) return true;
        else return false;
    }

    //Email 검증 결과에 따라 Response Entity 값 설정
    public ResponseEntity emailCheck(EmailDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return responseHandler.badRequest("이미 존재하는 이메일입니다.");
        } else {
            return responseHandler.singleSuccessResponse("사용 가능한 이메일입니다.");
        }
    }

    //닉네임 검증 결과에 따라 Response Entity 값 설정
    public ResponseEntity nicknameCheck(NicknameDto dto) {
        if (userRepository.findByNickname(dto.getNickname()).isPresent()) {
            return responseHandler.badRequest("이미 존재하는 닉네임입니다.");
        } else {
            return responseHandler.singleSuccessResponse("사용 가능한 이메일입니다.");
        }
    }

    //유저 기본 정보 받아오기
    public ResponseEntity<?> getUserInfo(Long id) {
        if (userRepository.findById(id).isPresent()) {
            User user = userRepository.findById(id).get();

            return responseHandler.userBasicInfo(UserBasicInfoResponse.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .nickname(user.getNickname())
                            .gender(user.getGender().getValue())
                            .stateMessage(user.getStateMessage())
                            .profileImage(user.getProfileImage())
                            .message("유저 정보가 성공적으로 조회되었습니다.")
                            .build());
        } else {
            return responseHandler.badRequest("유저 정보 조회에 실패했습니다.");
        }
    }

    //유저 정보 수정 및 Response Entity 반환
    public ResponseEntity<?> editProfile(Long id, EditProfileDto dto) {
        if (userRepository.findById(id).isPresent()) {
            User user = userRepository.findById(id).get();
            User updatedUser = User.builder()
                    .id(id)
                    .nickname(dto.getNickname())
                    .password(dto.getPassword())
                    .email(user.getEmail())
                    .gender(user.getGender())
                    .authority(Role.GENERAL)
                    .stateMessage(dto.getStateMessage())
                    .profileImage(dto.getProfile_image())
                    .build();
            userRepository.save(updatedUser);

            return responseHandler.userBasicInfo(UserBasicInfoResponse.builder()
                            .id(id)
                            .email(updatedUser.getEmail())
                            .nickname(updatedUser.getNickname())
                            .gender(updatedUser.getGender().getValue())
                            .stateMessage(updatedUser.getStateMessage())
                            .profileImage(updatedUser.getProfileImage())
                            .message("유저 정보가 수정되었습니다.")
                            .build());
        } else {
            return responseHandler.badRequest("유저 정보를 찾아내지 못했습니다.");
        }
    }

    //유저 삭제
    public ResponseEntity deleteUser(Long id) {
        userRepository.deleteById(id);
        return responseHandler.singleSuccessResponse("유저 정보가 삭제되었습니다.");
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

        if (userRepository.findByEmail(googleUser.getEmail()).isEmpty()) {
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

        if (userRepository.findByEmail(kakaoUserInfo.getEmail()).isEmpty()) {
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

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

}
