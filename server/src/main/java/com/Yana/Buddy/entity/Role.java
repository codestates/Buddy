package com.Yana.Buddy.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum Role {
    ADMIN("ADMIN"),
    GENERAL("GENERAL"),
    GUEST("GUEST");

    private String value;
}
