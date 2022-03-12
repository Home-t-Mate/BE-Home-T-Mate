package com.example.demo.model;

import com.example.demo.dto.commentdto.CommentRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 댓글 내용
    private String conmment;

    // 유저
    @ManyToOne
//            (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 게시글
    @ManyToOne
//            (fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(CommentRequestDto requestDto, Post post, User user) {
        this.conmment = requestDto.getConmment();
        this.post = post;
        this.user = user;
    }
}