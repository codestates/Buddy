package com.Yana.Buddy.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Data
@NoArgsConstructor
public class ChatMessage {

    @Id @GeneratedValue
    @Column(name = "chat_message_id")
    private Long id;

    public enum MessageType {
        ENTER, QUIT, TALK
    }

    private MessageType type;

    private String sender;

    private String message;

    private String createdAt;

    private String roomId;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom room;

    @Builder
    public ChatMessage(ChatRoom room, MessageType type, String sender, String message, String createdAt, String roomId) {
        this.room = room;
        this.type = type;
        this.sender = sender;
        this.message = message;
        this.createdAt = createdAt;
        this.roomId = roomId;
    }

}
