package com.Yana.Buddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GetEmailFromToken {

    private String email;
    private String message;

}
