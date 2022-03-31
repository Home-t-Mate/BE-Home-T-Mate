package com.example.demo.controller;

import com.example.demo.model.ChatMessage;
import com.example.demo.repository.ChatMessageRepository;
import com.example.demo.repository.RedisRepository;
import com.example.demo.security.jwt.JwtDecoder;
import com.example.demo.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.text.SimpleDateFormat;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final RedisRepository redisRepository;
    private final ChatService chatService;
    private final ChatMessageRepository chatMessageRepository;
    private final JwtDecoder jwtDecoder;


    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Header("Authorization") String token) throws InterruptedException {

        token = token.substring(7);
        message.setSender(jwtDecoder.decodeNickname(token));
        message.setProfileImg(jwtDecoder.decodeprofileImg(token));
        message.setUserCount(redisRepository.getUserCount(message.getRoomId()));
        chatMessageRepository.save(message);
        chatService.sendChatMessage(message);

    }
}
