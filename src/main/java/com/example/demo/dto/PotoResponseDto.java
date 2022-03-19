package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PotoResponseDto {
    private String postImg;

    public PotoResponseDto(String postImg) {
        this.postImg = postImg;
    }
}
