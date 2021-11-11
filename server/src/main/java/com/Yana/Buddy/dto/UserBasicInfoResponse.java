package com.Yana.Buddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserBasicInfoResponse {

    private Long id;
    private String email;
    private String nickname;
    private String stateMessage;
    private String profileImage;
    private String message;

}
