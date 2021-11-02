package com.Yana.Buddy.handler;

import com.Yana.Buddy.entity.ChatMessage;
import com.Yana.Buddy.service.ChatMessageService;
import com.Yana.Buddy.service.ChatRoomService;
import com.Yana.Buddy.service.TokenService;
import com.Yana.Buddy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Log4j2
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final TokenService tokenService;
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String token = accessor.getFirstNativeHeader("token");
            log.info("CONNECT -> JWT TOKEN : {}", token);
        }

        else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            /*
            header 정보에서 구독 destination 정보를 얻고 roomId 추출
            roomId는 URL 통해서 전송되고 있음
             */
            String roomId = chatMessageService.getRoomId(
                    Optional.ofNullable((String) message.getHeaders()
                            .get("simpDestintaion")).orElse("InvalidRoomId"));


            //채팅방에 들어온 클라이언트의 sessionId를 roomId와 매핑(추후 세션이 어떤 채팅방에 있는지 확인하기 위함)
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            chatRoomService.setEnterUserInfo(sessionId, roomId);

            //클라이언트 입장 메시지를 채팅방에 발송 (redis push)
            String token = Optional.ofNullable(accessor.getFirstNativeHeader("token")).orElse("UnknownUser");
            String name = userService.findUserByEmail(tokenService.checkJwtToken(token).get("email")).getNickname();
            chatMessageService.sendChatMessage(ChatMessage.builder()
                            .type(ChatMessage.MessageType.ENTER)
                            .roomId(roomId)
                            .sender(name)
                            .build());

            log.info("SUBSCRIBED {}, {}", name, roomId);
        }

        else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            //연결 종료된 클라이언트 sessionId 를 통해 채팅방 id 얻기
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = chatRoomService.getEnterUserId(sessionId);

            //클라이언트 퇴장 메시지를 채팅창에 발송 (redis publish)
            String token = Optional.ofNullable(accessor.getFirstNativeHeader("token")).orElse("UnknownUser");

            if (accessor.getFirstNativeHeader("token") != null) {
                String name = userService.findUserByEmail(tokenService.checkJwtToken(token).get("email")).getNickname();
                chatMessageService.sendChatMessage(ChatMessage.builder()
                        .type(ChatMessage.MessageType.QUIT)
                        .roomId(roomId)
                        .sender(name)
                        .build());
            }

            chatRoomService.removeEnterUserInfo(sessionId);
            log.info("DISCONNECT {}, {}", sessionId, roomId);
        }

        return message;
    }

}
