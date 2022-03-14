package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomRequestDto {
    String name;
    Long userCount;
    String content;
    String password;
    String roomImg;
    Boolean workOut = false;
}
