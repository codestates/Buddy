package com.Yana.Buddy.config;

import com.Yana.Buddy.handler.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //prefix /sub 로 수신 메시지 구분 (해당 주제를 가진 메시지만 핸들러로 라우팅 및 해당 주제에 가입한 모든 클라이언트에게 메시지 전송)
        config.enableSimpleBroker("/sub");
        //prefix /pub 로 발행 요청 (해당 메시지만 핸들러로 라우팅)
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chatting") //url/chatting 웹 소켓 연결
                .setAllowedOriginPatterns("*")
                .withSockJS(); //낮은 버전의 브라우저에서도 웹 소켓이 작동할 수 있도록 해줌
    }

    /**
    StompHandler 인터셉터 설정
    StompHandler 가 웹 소켓 앞단에서 token 및 메시지 TYPE 등을 체크할 수 있도록 설정
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }

}
