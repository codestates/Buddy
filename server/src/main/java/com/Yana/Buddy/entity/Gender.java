package com.Yana.Buddy.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum Gender {
    MALE("MAIL"),
    FEMALE("FEMALE");

    private String value;
}
