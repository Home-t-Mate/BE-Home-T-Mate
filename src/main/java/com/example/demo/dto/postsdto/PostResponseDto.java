package com.example.demo.dto.postsdto;

import com.example.demo.dto.PotoResponseDto;
import com.example.demo.dto.commentdto.CommentUserDto;
import com.example.demo.dto.likedto.LikeUserDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

// 게시글 전체 조회, 작성, 수정
@Getter
@Setter
public class PostResponseDto {
    private Long id;
    private Long userId;
    private String nickname;
    private String userImg;
    private String content;
    private String postsImg;
    private Long commentCount;
    private Long likeCount;
    private List<CommentUserDto> commentUserDto;
    private List<LikeUserDto> likeUserDto;
    private List<PotoResponseDto> potoResponseDto;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

//
//
//
    public PostResponseDto(Long id, Long userId, String nickname, String content, String postsImg ,
                           Long commentCount, Long likeCount,
                           List<CommentUserDto> commentUserDto, List<LikeUserDto> likeUserDto, List<PotoResponseDto> potoResponseDto,
                           String userImg, LocalDateTime createdAt, LocalDateTime modifiedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.content = content;
        this.postsImg = postsImg;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.commentUserDto = commentUserDto;
        this.likeUserDto = likeUserDto;
        this.potoResponseDto = potoResponseDto;
        this.userImg = userImg;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
