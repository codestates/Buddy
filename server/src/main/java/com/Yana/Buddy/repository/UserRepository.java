package com.Yana.Buddy.repository;

import com.Yana.Buddy.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    public void save(User user) {

    }

    public Optional<User> findByEmail(String email) {
        return null;
    }

    public Optional<User> findByNickname(String nickname) {
        return null;
    }

    public Optional<User> findById(Long id) {
        return null;
    }

    public void deleteById(Long id) {

    }

}
