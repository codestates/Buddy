package com.Yana.Buddy.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    @Id @GeneratedValue
    @Column(name = "chat_room_id")
    private Long id;

    private String name;

    private String subject;

    private String image;

    @Column(columnDefinition = "integer default 0")
    private int userCount;

    private String roomId;

    @Builder
    public ChatRoom(String name, String subject, String image) {
        this.name = name;
        this.subject = subject;
        this.image = image;
    }

    //테스트 전용 (추후 제거 예정)
    public static ChatRoom Create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }

}
