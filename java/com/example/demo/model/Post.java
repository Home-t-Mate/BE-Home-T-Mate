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


//    @Builder
    public Post(PostRequestDto requestDto, User user) {
        this.content = requestDto.getContent();
        this.user = user;
    }

    public Post(String postImg, String content, User user) {
        this.postImg = postImg;
        this.content = content;
        this.user = user;
    }
    public Post(String content, User user) {
        this.content = content;
        this.user = user;
    }
    public void update(PostRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

    public void update(String content, User user) {
        this.content = content;
        this.user = user;
    }
}