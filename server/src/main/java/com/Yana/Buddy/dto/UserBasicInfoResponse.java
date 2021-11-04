package com.Yana.Buddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBasicInfoResponse {

    private Long id;
    private String email;
    private String nickname;
    private String gender;
    private String stateMessage;
    private String profileImage;
    private String message;

}
