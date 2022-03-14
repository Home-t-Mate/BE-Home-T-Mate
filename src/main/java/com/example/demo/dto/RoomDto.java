package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDto {
    String name;
    Long userCount;
    String content;
    String password;
}
