package com.Yana.Buddy.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.*;

@Entity
@Data
@NoArgsConstructor
public class ChatMessage {

    @Id @GeneratedValue
    @Column(name = "chat_message_id")
    private Long id;

    @AllArgsConstructor @Getter
    public enum MessageType {
        ENTER("ENTER"), QUIT("QUIT"), TALK("TALK");

        private String value;
    }

    @Enumerated(STRING)
    private MessageType type;

    private String sender;

    private String message;

    private String createdAt;

    private String roomId;

    @Builder
    public ChatMessage(MessageType type, String sender, String message, String createdAt, String roomId) {
        this.type = type;
        this.sender = sender;
        this.message = message;
        this.createdAt = createdAt;
        this.roomId = roomId;
    }

}
