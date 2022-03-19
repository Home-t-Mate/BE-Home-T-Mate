//package com.example.demo.model;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Entity
////@AllArgsConstructor(access = AccessLevel.PROTECTED)
//@NoArgsConstructor
//@Getter
////@Table(name = "post_img")
//public class PostImg {
//
////    private final Post Post;
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Id
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(nullable = false)
//    private Post post;
//
//    @Column(nullable = false)
//    private String originalFileName;
//
//    @Column(nullable = false)
//    private String filePath;
//
//    public PostImg(String originalFileName, String filePath, Post post) {
//        this.originalFileName = originalFileName;
//        this.filePath = filePath;
////        this.Post = post;
//    }
//}
