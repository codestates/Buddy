package com.Yana.Buddy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @NotEmpty
    private String email;

    private String password;

    private String nickname;

    private String stateMessage;

    private String profileImage;

    @Enumerated(STRING)
    private Gender gender;

    @Enumerated(STRING)
    private Role authority;

    public User update(String email, String picture) {
        this.email = email;
        this.profileImage = picture;
        return this;
    }

}
