package com.Yana.Buddy.entity;

import com.Yana.Buddy.dto.ChatRoomRequestDto;
import com.Yana.Buddy.service.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    private String name;

    private String image;

    private String subject;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id_room")
    private User user;

    public ChatRoom(ChatRoomRequestDto dto, UserService userService) {
        this.name = dto.getName();
        this.image = dto.getImage();
        this.subject = dto.getSubject();
        this.user = userService.findUserById(dto.getUserId());
    }

}
