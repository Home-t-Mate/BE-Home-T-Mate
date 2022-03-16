package com.example.demo.model;

import com.example.demo.dto.postsdto.PostRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@Table(name = "post")
public class Post extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    // 내용
    @Column()
    private String content;

    // 사용자
    @ManyToOne
//            (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    @Column(nullable = false)
//    private Long userId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(nullable = false)
//    private SkiResort skiResort;

//    // 제목
//    @Column(nullable = false)
//    private String title;



//    @Column
//    private int likeCnt;
//
//    @Column
//    private int commentCnt;

    // 게시글 이미지
    @Column(columnDefinition = "mediumblob")
    private String postImg;

//    // 댓글
//    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
//    @JsonIgnoreProperties({"post"})
//    private List<Comment> commentList;
//
//    // 좋아요
//    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
//    @JsonIgnoreProperties({"post"})
//    private List<Like> likeList;


    @Builder
    public Post(PostRequestDto requestDto, User user) {
        this.content = requestDto.getContent();
        this.postImg = requestDto.getPostImg();
        this.user = user;
    }

    public void update(PostRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}