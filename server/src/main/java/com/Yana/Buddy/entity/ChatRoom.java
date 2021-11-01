package com.Yana.Buddy.entity;

import com.Yana.Buddy.dto.ChatRoomRequestDto;
import com.Yana.Buddy.service.UserService;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
public class ChatRoom extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "chat_room_id")
    private Long id;

    private String name;

    private String image;

    private String subject;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public ChatRoom(ChatRoomRequestDto dto, UserService userService) {
        this.name = dto.getName();
        this.image = dto.getImage();
        this.subject = dto.getSubject();
        this.user = userService.findUserById(dto.getUserId());
    }

}
