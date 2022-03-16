package com.example.demo.model;

import com.example.demo.dto.postsdto.PostRequestDto;
import lombok.*;

import javax.persistence.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@Table(name = "post")
public class Post extends Timestamped {
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;


    @Column()
    private String postImg;
    // 내용

    @Column()
    private String content;


    // 사용자
//            (fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn()
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


//    // 댓글
//    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
//    @JsonIgnoreProperties({"post"})
//    private List<Comment> commentList;
//
//    // 좋아요
//    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
//    @JsonIgnoreProperties({"post"})
//    private List<Like> likeList;


//    @Builder
    public Post(PostRequestDto requestDto, User user) {
        this.postImg = requestDto.getPostImg();
        this.content = requestDto.getContent();
        this.user = user;
    }

    public Post(String postImg, String content, User user) {
        this.postImg = postImg;
        this.content = content;
        this.user = user;
    }

    public void update(PostRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}