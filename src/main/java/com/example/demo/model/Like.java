package com.example.demo.model;

import com.example.demo.dto.likedto.LikeRequestDto;
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
@Table(name = "like_table")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 게시글
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Like(LikeRequestDto requestDto) {
        this.user = requestDto.getUser();
        this.post = requestDto.getPost();
    }
}