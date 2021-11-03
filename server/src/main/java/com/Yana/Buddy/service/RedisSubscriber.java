package com.Yana.Buddy.service;

import com.Yana.Buddy.entity.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@Log4j2
@Service
@RequiredArgsConstructor
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    //Redis 에서 메시지가 publish 되면 대기하고 있던 Redis Subscriber 가 해당 메시지를 받아서 처리
    public void sendMessage(String publishMessage) {
        log.info("메시지 pub 확인 : {}", publishMessage);
        try {
            ChatMessage message = objectMapper.readValue(publishMessage, ChatMessage.class);
            messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        } catch (JsonMappingException e) {
            log.error("JsonMappingException");
        } catch (JsonProcessingException e) {
            log.error("JsomProcessingException");
        }
    }

}
