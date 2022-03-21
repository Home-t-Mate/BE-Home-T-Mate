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
    private String nickname;
    private String content;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getComment();
    }
}