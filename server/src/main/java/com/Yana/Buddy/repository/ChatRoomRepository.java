package com.Yana.Buddy.repository;

import com.Yana.Buddy.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    ChatRoom findByRoomId(String roomId);
    List<ChatRoom> findByUserCount(int count);

}
