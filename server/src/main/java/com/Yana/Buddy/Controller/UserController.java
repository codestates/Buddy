package com.Yana.Buddy.Controller;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class UserController {

    @PostMapping("/login")
    ResponseEntity login(){
        return null;
    }

    @PostMapping("/signup")
    ResponseEntity signup(){
        return null;
    }

    @GetMapping("/email-duplication-check/{email}")
    ResponseEntity emailCheck(){
        return null;
    }

    @GetMapping("/nickname-duplication-check/{nickname}")
    ResponseEntity nicknameCheck(){
        return null;
    }

    @GetMapping("/token-valid-check")
    ResponseEntity tokenCheck(){
        return null;
    }

    @GetMapping("/renewal-token")
    ResponseEntity renewalToke(){
        return null;
    }

    @GetMapping("/profile/{id}")
    ResponseEntity userProfile(){
        return null;
    }

    @PutMapping("/profile/{id}")
    ResponseEntity modifyUserInfo(){
        return null;
    }
}
