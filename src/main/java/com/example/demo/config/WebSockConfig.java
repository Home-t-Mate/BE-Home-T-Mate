package com.example.demo.config;

import com.example.demo.config.handler.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }

//ws-youtube 추가하였음

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        System.out.println("스톰프 호출");
        WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
        registry.addEndpoint("/ws-stomp", "/ws-youtube").setAllowedOriginPatterns("*")
//        registry.addEndpoint("/ws-stomp").setAllowedOrigins("*").setAllowedOriginPatterns("*")
                .withSockJS(); // sock.js를 통하여 낮은 버전의 브라우저에서도 websocket이 동작할수 있게 합니다.
    }


        @Override
        public void configureClientInboundChannel (ChannelRegistration registration){
            System.out.println("핸들러 호출");
            registration.interceptors(stompHandler);
        }


    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(160 * 64 * 1024); // default : 64 * 1024
        registration.setSendTimeLimit(100 * 10000); // default : 10 * 10000
        registration.setSendBufferSizeLimit(3* 512 * 1024); // default : 512 * 1024
    }

    }