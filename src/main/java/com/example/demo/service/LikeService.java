package com.example.demo.service;

import com.example.demo.dto.likedto.LikeRequestDto;
import com.example.demo.dto.likedto.LikeResponseDto;
import com.example.demo.model.Like;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.LikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public LikeResponseDto addLike(Long postId, Long uid) {
        User user = userRepository.findById(uid).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 없습니다.")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                ()->new IllegalArgumentException("게시글이 없습니다.")
        );

        Like findLike = likeRepository.findByUserAndPost(user,post).orElse(null);

        if(findLike == null){
            LikeRequestDto requestDto = new LikeRequestDto(user, post);
            Like like = new Like(requestDto);
            likeRepository.save(like);
        } else {
            likeRepository.deleteById(findLike.getId());
        }
        return new LikeResponseDto(postId, likeRepository.countByPost(post));
    }
}

