package com.Yana.Buddy.service;

import com.Yana.Buddy.dto.ProhibiedUserDto;
import com.Yana.Buddy.entity.ProhibiedUserEntity;
import com.Yana.Buddy.entity.User;
import com.Yana.Buddy.repository.ProhibiedUserRepository;
import com.Yana.Buddy.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProhibiedUserService {

    private final ProhibiedUserRepository prohibiedUserRepository;
    private final UserRepository userRepository;

    public ProhibiedUserService(ProhibiedUserRepository prohibiedUserRepository, UserRepository userRepository) {
        this.prohibiedUserRepository = prohibiedUserRepository;
        this.userRepository = userRepository;
    }

    public void createProbidied(ProhibiedUserDto prohibiedUserDto) {
        User user = userRepository.findByNickname(prohibiedUserDto.getUserNickname()).get();
        ProhibiedUserEntity use = new ProhibiedUserEntity();
        use.setUser(user);
        use.setLogImg(prohibiedUserDto.getLogImg());
        use.setReason(prohibiedUserDto.getReason());
        prohibiedUserRepository.save(use);
    }
}
