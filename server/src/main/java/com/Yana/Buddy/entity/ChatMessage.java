package com.Yana.Buddy.entity;

import com.Yana.Buddy.dto.ChatMessageRequestDto;
import com.Yana.Buddy.service.UserService;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class ChatMessage {

    public enum MessageType {
        ENTER, TALK, QUIT
    }

    @Id @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    private MessageType type;

    private String roomId;

    private int userCount;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id_message")
    private User user;

    //Redis Message Listener 로 웹 소켓을 통해 바로 채팅창에 메시지를 전달해주기 위한 별도의 컬럼들
    private Long chatUserId;
    private String sender;
    private String message;
    private String createdAt;

    @Builder
    public ChatMessage(MessageType type, String roomId, Long chatUserId, String sender, String message, String createdAt) {
        this.type = type;
        this.roomId = roomId;
        this.user = null;
        this.chatUserId = chatUserId;
        this.sender = sender;
        this.message = message;
        this.createdAt = createdAt;
    }

    @Builder
    public ChatMessage(ChatMessageRequestDto dto) {
        this.type = dto.getType();
        this.roomId = dto.getRoomId();
        this.user = null;
        this.chatUserId = dto.getChatUserId();
        this.sender = dto.getSender();
        this.message = dto.getMessage();
        this.createdAt = dto.getCreatedAt();
    }

    @Builder
    public ChatMessage(ChatMessageRequestDto dto, UserService userService) {
        this.type = dto.getType();
        this.roomId = dto.getRoomId();
        this.user = userService.findUserById(dto.getChatUserId());
        this.chatUserId = dto.getChatUserId();
        this.sender = dto.getSender();
        this.message = dto.getMessage();
        this.createdAt = dto.getCreatedAt();
    }

}
