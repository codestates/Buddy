package com.Yana.Buddy.dto;

import com.Yana.Buddy.entity.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.email = user.getEmail();
        this.picture = user.getProfileImage();
    }
}
