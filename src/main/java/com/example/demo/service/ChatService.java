package com.example.demo.service;

import com.example.demo.model.ChatMessage;
import com.example.demo.model.Room;
import com.example.demo.repository.RedisRepository;
import com.example.demo.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final RedisRepository redisRepository;
    private final RoomRepository roomRepository;

    /**
     * destination정보에서 roomId 추출
     */
    public Long getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1) {
            return Long.parseLong(destination.substring(lastIndex + 1));
        } else {
            return null;
        }
    }

    /**
     * 채팅방에 메시지 발송
     */
    //rtc 처리
    public void sendChatMessage(ChatMessage chatMessage) {
        chatMessage.setUserCount(redisRepository.getUserCount(chatMessage.getRoomId()));

        System.out.println("레디스 레포 getUserCount 조회");
        System.out.println(chatMessage.getType());

        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
            chatMessage.setSender("[알림]");


        } else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
            chatMessage.setSender("[알림]");
        }

        String sender = chatMessage.getSender();
        Long roomId = chatMessage.getRoomId();

        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
//    }
    }
}

