package com.Yana.Buddy.repository;

import com.Yana.Buddy.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByRoomId(Long roomId);
    void deleteByRoomId(String roomId);

}
