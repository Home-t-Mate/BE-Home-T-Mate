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

        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
            chatMessage.setSender("[알림]");

            String sender = chatMessage.getSender();
            Long roomId = chatMessage.getRoomId();

            switch (chatMessage.getType()) {
                case OFFER:
                case ANSWER:
                case ICE:
                    System.out.println("영상 타입 진입");
                    Object candidate = chatMessage.getCandidate();
                    Object sdp = chatMessage.getSdp();
//                    if (candidate != null) {
//                        candidate.toString().substring(0, 64);
//                    } else {
//                        sdp.toString().substring(0,64);
//                    }
                    List<Room> rm = roomRepository.findByRoomId(roomId);
                    if (rm != null) {
                        for (Room room : rm) {
                            if (!room.getUser().getUsername().equals(chatMessage.getSender())) {
                                sendChatMessage(new ChatMessage(chatMessage.getType(), roomId, sender, chatMessage.getMessage(), chatMessage.getUserCount(), (String) candidate, (String) sdp));
                                System.out.println("set메시지");
                            }

                        }

                    }
            }
        } else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
            chatMessage.setSender("[알림]");
        }


        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }

}
