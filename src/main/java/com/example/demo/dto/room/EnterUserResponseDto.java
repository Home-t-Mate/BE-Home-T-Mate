package com.example.demo.dto.room;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnterUserResponseDto {
    private String nickname;
    private String profileImg;

    public EnterUserResponseDto(String nickname, String profileImg) {
        this.nickname = nickname;
        this.profileImg = profileImg;
    }
}
