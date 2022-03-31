package com.example.demo.service;

import com.example.demo.model.ChatMessage;
import com.example.demo.model.EnterUser;
import com.example.demo.model.Room;
import com.example.demo.model.User;
import com.example.demo.repository.EnterUserRepository;
import com.example.demo.repository.RedisRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.TimeConversion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final RedisRepository redisRepository;
    private final RoomRepository roomRepository;
    private final EnterUserRepository enterUserRepository;
    private final UserRepository userRepository;


//     destination정보에서 roomId 추출
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1) {
            return destination.substring(lastIndex + 1);
        } else {
            return "";
        }
    }


    public void sendChatMessage(ChatMessage chatMessage) throws InterruptedException {

        chatMessage.setUserCount(redisRepository.getUserCount(chatMessage.getRoomId()));


        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            Thread.sleep(2000);
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
            chatMessage.setSender("[알림]");

        }
        if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {

            chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
            chatMessage.setSender("[알림]");

        }

        if(ChatMessage.MessageType.YOUTUBEURL.equals(chatMessage.getType())) {
            Room room = roomRepository.findByroomId(chatMessage.getRoomId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
            room.setWorkOut(true);
            roomRepository.save(room);
        }
        if(ChatMessage.MessageType.YOUTUBESTOP.equals(chatMessage.getType())) {
            Room room = roomRepository.findByroomId(chatMessage.getRoomId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
            room.setWorkOut(false);
            roomRepository.save(room);
        }

        if(ChatMessage.MessageType.YOUTUBEPAUSE.equals(chatMessage.getType())) {
            Room room = roomRepository.findByroomId(chatMessage.getRoomId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
            room.setWorkOut(false);
            roomRepository.save(room);
        }

        if(ChatMessage.MessageType.VIDEOON.equals(chatMessage.getType())) {
            ChatMessage.builder().type(ChatMessage.MessageType.ENTER);
        }

        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }
}

