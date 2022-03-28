package com.example.demo.config.handler;

import com.example.demo.model.ChatMessage;
import com.example.demo.model.Room;
import com.example.demo.repository.EnterUserRepository;
import com.example.demo.repository.RedisRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtDecoder;
import com.example.demo.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;
    private final ChatService chatService;
    private final RedisRepository redisRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final EnterUserRepository enterUserRepository;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // websocket 연결시 헤더의 jwt token 검증
        if (StompCommand.CONNECT == accessor.getCommand()) {
            jwtDecoder.decodeUsername(accessor.getFirstNativeHeader("Authorization").substring(7));

        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            String roomId = chatService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
            String sessionId = (String) message.getHeaders().get("simpSessionId");

            redisRepository.setUserEnterInfo(sessionId, roomId);
            redisRepository.plusUserCount(roomId);
//            String name = jwtDecoder.decodeUsername(accessor.getFirstNativeHeader("Authorization").substring(7));
            String name = jwtDecoder.decodeNickname(accessor.getFirstNativeHeader("Authorization").substring(7));


            try {
                chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.ENTER).roomId(roomId).sender(name).build());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println("5");
//            System.out.println("SUBSCRIBE 클라이언트 헤더" + message.getHeaders());
//            System.out.println("SUBSCRIBE 클라이언트 세션 아이디" + sessionId);
//            System.out.println("SUBSCRIBE 클라이언트 유저 이름: " + name);
//                chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.ENTER).roomId(roomId).sender(name).build());
            System.out.println(redisRepository.getUserCount("roomId:" +  roomId));

            if (roomId != null) {
                Room room = roomRepository.findByroomId(roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
                room.setUserCount(redisRepository.getUserCount(roomId));
                roomRepository.save(room);
            }

        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            String sessionId = (String) message.getHeaders().get("simpSessionId");
//            System.out.println("DISCONNECT 클라이언트 sessionId: " + sessionId);

            String roomId = redisRepository.getUserEnterRoomId(sessionId);

            redisRepository.minusUserCount(roomId);


            System.out.println("roomid:" + roomId);
            if (roomId != null) {
                Room room = roomRepository.findByroomId(roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다.(DISCONNECT)"));
                room.setUserCount(redisRepository.getUserCount(roomId));
                roomRepository.save(room);
            }


            String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
            redisRepository.removeUserEnterInfo(sessionId);


            //유튜브 켜고 방 나왔을 때, 방 인원이 0명이면 false로
            if(redisRepository.getUserCount(roomId) == 0) {
                Room room = roomRepository.findByroomId(roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
                room.setWorkOut(false);
                roomRepository.save(room);
            }

        }
        return message;
    }
}
