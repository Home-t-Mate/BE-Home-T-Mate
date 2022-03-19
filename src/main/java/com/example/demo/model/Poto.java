package com.example.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.messaging.handler.annotation.SendTo;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Poto {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String postImg;

    @JoinColumn
    @ManyToOne
    private Post post;

    public Poto(String postImg, Post post) {
        this.postImg = postImg;
        this.post = post;
    }
}
