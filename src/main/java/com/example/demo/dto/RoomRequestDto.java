package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomRequestDto {
    String name;
    String roomId;
    Long userCount;
    String content;
    String password;
    String roomImg;
    Boolean passCheck;
    Boolean workOut;


}
