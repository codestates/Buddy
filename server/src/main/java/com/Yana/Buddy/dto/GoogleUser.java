package com.Yana.Buddy.dto;

import com.Yana.Buddy.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUser {

    public String id;
    public String email;
    public Boolean verified_email;
    public String name;
    public String given_name;
    public String family_name;
    public String picture;
    public String locale;

    public User toUser() {
        return User.builder()
                .email(email)
                .profileImage(picture)
                .build();
    }

}
