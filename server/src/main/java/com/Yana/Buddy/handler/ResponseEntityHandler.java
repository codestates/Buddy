package com.Yana.Buddy.handler;

import com.Yana.Buddy.dto.ChatRoomResponse;
import com.Yana.Buddy.dto.LoginSuccessResponse;
import com.Yana.Buddy.dto.UserBasicInfoResponse;
import com.Yana.Buddy.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseEntityHandler {

    public ResponseEntity badRequest(String message) {
        return ResponseEntity.status(400).body(message);
    }

    public ResponseEntity singleSuccessResponse(String message) {
        return ResponseEntity.status(200).body(message);
    }

    public ResponseEntity<User> userEntireInfo(User user) {
        return ResponseEntity.status(200).body(user);
    }

    public ResponseEntity<LoginSuccessResponse> loginSuccess(LoginSuccessResponse dto) {
        return ResponseEntity.status(200).body(dto);
    }

    public ResponseEntity<UserBasicInfoResponse> userBasicInfo(UserBasicInfoResponse dto) {
        return ResponseEntity.status(200).body(dto);
    }

    public ResponseEntity<ChatRoomResponse> chatRoomInfo(ChatRoomResponse dto) {
        return ResponseEntity.status(200).body(dto);
    }

}
