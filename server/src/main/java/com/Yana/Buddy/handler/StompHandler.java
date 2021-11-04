package com.Yana.Buddy.handler;

import com.Yana.Buddy.entity.ChatMessage;
import com.Yana.Buddy.repository.ChatRoomRepository;
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

import java.security.Principal;
import java.util.Optional;

@Log4j2
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final TokenService tokenService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    //WebSocket 을 통해 들어온 요청이 처리 되기전 실행
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // WebSocket 연결시 헤더의 jwt token 검증
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String jwtToken = accessor.getFirstNativeHeader("token");
            log.info("CONNECT : {}", jwtToken);
        }

        /**
         1. header 정보에서 구독 destination 정보를 얻고 roomId 추출
         2. 채팅방에 들어온 클라이언트 sessionId를 roomId와 매핑 (추후 특정 세션이 어느 방에 들어가 있는지 확인하는 용도)
         3. 채팅방 인원++
         4. 클라이언트 입장 메시지를 채팅방에 발송 (redis publish)
         */
        else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            String roomId = chatMessageService.getRoomId(Optional.ofNullable(
                    (String) message.getHeaders().get("simpDestination"))
                    .orElse("Anonymous User"));
            log.info("SUBSCRIBE - 구독 요청 방 : {}", roomId);

            String sessionId = (String) message.getHeaders().get("simpSessionId");
            log.info("SUBSCRIBE - 요청한 session id : {}", sessionId);
            chatRoomService.setUserEnterInfo(sessionId, roomId);

            String name = userService.findUserByEmail(
                    tokenService.checkJwtToken(accessor.getFirstNativeHeader("token")).getEmail()
            ).get().getNickname();

            /** 참조한 소스 코드에서는 아래의 코드를 사용했고 이를 똑같이 적용해보려 했지만 잘 적용되지 않아서 토큰에서 추출하는 방식으로 진행함
            String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser"))
                    .map(Principal::getName).orElse("Anonymous User");
             */

            log.info("SUBSCRIBE - 구독 요청한 유저 이름 : {}", name);
            chatMessageService.sendChatMessage(ChatMessage.builder()
                            .type(ChatMessage.MessageType.ENTER)
                            .roomId(roomId)
                            .sender(name)
                            .build());

            log.info("SUBSCRIBED {}, {}", name, roomId);
        }

        /**
         1. 연결이 종료된 클라이언트 sessionId를 통해서 채팅방 id 획득
         2. 채팅방 인원--
         3. 클라이언트 퇴장 메시지를 채팅방에 발송 (redis publish)
         */
        else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = chatRoomService.getUserEnterRoomId(sessionId);

            String name = userService.findUserByEmail(
                    tokenService.checkJwtToken(accessor.getFirstNativeHeader("token")).getEmail()
            ).get().getNickname();

            /** subscribe 와 동일한 이유로, 유저의 닉네임을 토큰을 통해 따로 가져옴
            String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser"))
                    .map(Principal::getName).orElse("Anonymous User");
             */

            chatMessageService.sendChatMessage(ChatMessage.builder()
                            .type(ChatMessage.MessageType.QUIT)
                            .roomId(roomId)
                            .sender(name)
                            .build());
            chatRoomService.removeUserEnterInfo(sessionId);

            log.info("DISCONNECTED {}, {}", sessionId, roomId);
        }

        return message;
    }

}
