package com.example.demo.model;

import com.example.demo.dto.TodolistRequestDto;
import com.example.demo.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Todolist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Boolean completed;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startAt;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column
    private String time;


    public Todolist(TodolistRequestDto requestDto, User user) {
        this.user = user;
        this.title = requestDto.getTitle();
        this.completed = requestDto.getCompleted();
        this.startAt = requestDto.getStartAt();
        this.endAt = requestDto.getEndAt();
        this.time = requestDto.getTime();
    }
}

