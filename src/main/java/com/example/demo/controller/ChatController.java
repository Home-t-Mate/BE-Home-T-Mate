package com.example.demo.controller;

import com.example.demo.model.ChatMessage;
import com.example.demo.repository.ChatMessageRepository;
import com.example.demo.repository.RedisRepository;
import com.example.demo.security.jwt.JwtDecoder;
import com.example.demo.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

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
    public void message(ChatMessage message, @Header("Authorization") String token) {
//        String nickname = jwtTokenProvider.getUserNameFromJwt(token);
        // 로그인 회원 정보로 대화명 설정
//        System.out.println(token);
        token = token.substring(7);
        message.setSender(jwtDecoder.decodeUsername(token));
        System.out.println("타입 내용: " +message.getType());
        System.out.println("메시지 내용: " +message.getMessage());
        System.out.println("룸아이디 내용: " +message.getRoomId());
        // 채팅방 인원수 세팅
        message.setUserCount(redisRepository.getUserCount(message.getRoomId()));
        // Websocket에 발행된 메시지를 redis로 발행(publish)
        chatMessageRepository.save(message);
        chatService.sendChatMessage(message);

        //여기는 됨
    }
}
