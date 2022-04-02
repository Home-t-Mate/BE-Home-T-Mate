package com.example.demo.dto.postsdto;

import com.example.demo.dto.PhotoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostCreateResponseDto {
    private Long id;
    private Long userId;
    private String userImg;
    private String nickname;
    private String content;
    private List<PhotoResponseDto> photoResponseDto;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
