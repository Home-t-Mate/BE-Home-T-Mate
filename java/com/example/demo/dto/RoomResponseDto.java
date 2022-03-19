package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RoomResponseDto {
    private String name;
    private String roomId;
    private String roomImg;
    private String content;
    private Long userCount;
    private Boolean passCheck;
    private Boolean workOut;
    private int count;


    public RoomResponseDto(String name, String roomId, String roomImg, String content, Boolean passCheck,Boolean workOut, int count) {
        this.name = name;
        this.roomId = roomId;
        this.roomImg = roomImg;
        this.content = content;
        this.passCheck = passCheck;
        this.workOut = workOut;
        this.count = count;

    }

}
