package com.example.demo.controller;

import com.example.demo.dto.likedto.LikeResponseDto;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class LikeController {

    private final LikeService likeService;

//    like (좋아요) 누르기
    @PostMapping("/api/like/{postId}")
    public LikeResponseDto Like(@PathVariable Long postId,
                                @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return likeService.addLike(postId, userDetails.getUser().getId());
    }
}
