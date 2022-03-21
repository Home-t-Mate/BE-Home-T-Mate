package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoResponseDto {
    private String postImg;

    public PhotoResponseDto(String postImg) {
        this.postImg = postImg;
    }
}
