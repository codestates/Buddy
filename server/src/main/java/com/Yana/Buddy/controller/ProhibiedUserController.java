package com.Yana.Buddy.controller;

import com.Yana.Buddy.dto.ProhibiedUserDto;
import com.Yana.Buddy.service.ProhibiedUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;

@RestController
@RequestMapping("/prohibied")
public class ProhibiedUserController {
    private final ProhibiedUserService prohibiedUserService;

    public ProhibiedUserController(ProhibiedUserService prohibiedUserService) {
        this.prohibiedUserService = prohibiedUserService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> createProbidiedUser(@RequestBody ProhibiedUserDto prohibiedUserDto){
        prohibiedUserService.createProbidied(prohibiedUserDto);
        return null;
    }
}
