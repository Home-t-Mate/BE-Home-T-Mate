package com.example.demo.repository;

import com.example.demo.model.Like;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);

    Long countByPost(Post post);

    Long deleteByPost(Post post);

    List<Like> findAllByPost(Post post);

    void deleteAllByPost(Post post);
}
