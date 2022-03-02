package com.example.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatMessageRequestDto {
    ChatMessage.MessageType type;
    Long roomId;
    String sender;
    String message;
    Long userCount;
}
