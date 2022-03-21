package com.example.demo.dto.likedto;

import com.example.demo.model.Post;
import com.example.demo.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class LikeRequestDto {
    private User user;
    private Post post;
}