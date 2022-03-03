package com.example.demo.dto;

import com.example.demo.model.ChatMessage;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequestDto {
    private ChatMessage.MessageType type;
    private Long roomId;
    private String sender;
    private String message;
    private long userCount;
}
