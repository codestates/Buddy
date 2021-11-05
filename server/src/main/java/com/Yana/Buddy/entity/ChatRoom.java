package com.Yana.Buddy.entity;

import lombok.AllArgsConstructor;
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

    private String roomId;

    private int userCount;

    @Builder
    public ChatRoom(String roomId, int userCount) {
        this.roomId = roomId;
        this.userCount = userCount;
    }

}
