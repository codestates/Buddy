package com.Yana.Buddy.controller;

import com.Yana.Buddy.dto.EmailDto;
import com.Yana.Buddy.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/email_confirm")
    public ResponseEntity<?> emailConfirm(@RequestBody (required = true) EmailDto email) throws  Exception{
        String confirm = emailService.sendSimpleMessage(email.getEmail());
        return ResponseEntity.status(200).body(confirm);
    }
    
}
