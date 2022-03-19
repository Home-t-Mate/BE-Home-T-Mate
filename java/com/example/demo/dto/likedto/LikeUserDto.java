package com.example.demo.dto.likedto;

import com.example.demo.model.Like;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeUserDto {
    private Long userId;
    private String nickname;

    public LikeUserDto(Like like) {
        this.userId = like.getUser().getId();
        this.nickname = like.getUser().getNickname();
    }
}
