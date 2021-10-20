package com.Yana.Buddy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import static javax.persistence.EnumType.*;

@Entity
@Getter
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @NotEmpty
    private String email;

    private String password;

    @NotEmpty
    private String stateMessage;

    @Enumerated(STRING)
    private Gender gender;

    @Enumerated(STRING)
    private Role authority;

}
