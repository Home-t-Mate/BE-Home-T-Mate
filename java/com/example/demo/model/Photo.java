package com.example.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Photo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String postImg;

    @JoinColumn
    @ManyToOne
    private Post post;

    public Photo(String postImg, Post post) {
        this.postImg = postImg;
        this.post = post;
    }
}
