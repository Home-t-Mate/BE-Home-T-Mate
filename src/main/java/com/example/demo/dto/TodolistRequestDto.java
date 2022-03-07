package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodolistRequestDto {

    private String title;
    private Boolean completed;
    private Date startAt;
    private Date endAt;
    private String time;

}
