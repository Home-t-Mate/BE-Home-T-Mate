package com.example.demo.dto.room;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoomResponseDto {
    private String name;
    private String roomId;
    private String roomImg;
    private String content;
    private Long userCount;
    private Boolean passCheck;
    private Boolean workOut;
    private String nickname;
    private String profileImg;


    public RoomResponseDto(String name, String roomId, String roomImg, String content,Long userCount, Boolean passCheck,Boolean workOut) {
        this.name = name;
        this.roomId = roomId;
        this.roomImg = roomImg;
        this.content = content;
        this.userCount = userCount;
        this.passCheck = passCheck;
        this.workOut = workOut;

    }

    public RoomResponseDto(String name, String roomId, String roomImg, String content, Long userCount, Boolean passCheck, Boolean workOut, String nickname, String profileImg) {
        this.name = name;
        this.roomId = roomId;
        this.roomImg = roomImg;
        this.content = content;
        this.userCount = userCount < 0 ? 0 : userCount;
        this.passCheck = passCheck;
        this.workOut = workOut;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }

}
