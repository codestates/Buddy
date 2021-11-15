package com.Yana.Buddy.controller;

import com.Yana.Buddy.dto.CodeDto;
import com.Yana.Buddy.dto.EmailDto;
import com.Yana.Buddy.handler.ResponseEntityHandler;
import com.Yana.Buddy.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private String inputCode;
    private final ResponseEntityHandler responseHandler;

    @PostMapping("/email_confirm")
    public ResponseEntity<?> emailConfirm(@RequestBody EmailDto email) throws Exception {
        try {
            inputCode = emailService.sendSimpleMessage(email.getEmail());
            return responseHandler.singleSuccessResponse("OK");
        } catch (NullPointerException e){
            log.error("Null Pointer Exception : " + e);
            return null;
        }

    }

    @PostMapping("/email_code_check")
    public ResponseEntity<?> emailCodeCheck(@RequestBody CodeDto code){
        if (inputCode.equals(code.getCode())){
            return responseHandler.singleSuccessResponse("OK");
        } else {
            return responseHandler.badRequest("인증 정보가 틀렸습니다.");
        }
    }

}
