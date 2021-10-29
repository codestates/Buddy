package com.Yana.Buddy.controller;

import com.Yana.Buddy.dto.CodeDto;
import com.Yana.Buddy.dto.EmailDto;
import com.Yana.Buddy.service.EmailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Log4j2
@RestController
public class EmailController {

    private final EmailService emailService;
    private String inputCode;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/email_confirm")
    public ResponseEntity<?> emailConfirm(@RequestBody EmailDto email) throws Exception {
        try {
            inputCode = emailService.sendSimpleMessage(email.getEmail());
            return ResponseEntity.status(200).body(new HashMap<>(){
                {
                    put("message", "ok");
                }
            });
        } catch (NullPointerException e){
            log.error("Null Pointer Exception : " + e);
            return null;
        }

    }

    @PostMapping("/email_code_check")
    public ResponseEntity<?> emailCodeCheck(@RequestBody CodeDto code){
        if (inputCode.equals(code.getCode())){
            return ResponseEntity.status(200).body(new HashMap<>(){
                {
                    put("message", "ok");
                }
            });
        } else {
            return ResponseEntity.status(400).body(new HashMap<>(){
                {
                    put("message", "인증번호가 틀렸습니다");
                }
            });
        }
    }

}
