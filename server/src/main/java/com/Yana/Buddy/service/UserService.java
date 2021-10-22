package com.Yana.Buddy.service;

import com.Yana.Buddy.dto.EditProfileDto;
import com.Yana.Buddy.dto.RegisterDto;
import com.Yana.Buddy.entity.Gender;
import com.Yana.Buddy.entity.Role;
import com.Yana.Buddy.entity.User;
import com.Yana.Buddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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

}
