package com.Yana.Buddy.controller;

import com.Yana.Buddy.entity.ChatRoom;
import com.Yana.Buddy.repository.ChatRoomRepository;
import com.Yana.Buddy.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class TestController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/rooms")
    public String room(Model model) { return "/test"; }

    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/detail";
    }

    @PostMapping("/test")
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        return chatRoomService.testChatRoom(name);
    }

}
