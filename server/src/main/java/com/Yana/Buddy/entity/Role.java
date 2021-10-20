package com.Yana.Buddy.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum Role {
    ADMIN("ADMIN"),
    GENERAL("GENERAL");

    private String value;
}
