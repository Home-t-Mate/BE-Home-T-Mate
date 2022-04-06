package com.example.demo.dto.commentdto;

import com.example.demo.model.Comment;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentResponseDto {
    private Long commentId;
    private Long userId;
    private String nickname;
    private String comment;
    private String ProfileImg;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.userId = comment.getUser().getId();
        this.nickname = comment.getUser().getNickname();
        this.comment = comment.getComment();
        this.ProfileImg = comment.getUser().getProfileImg();

    }
}