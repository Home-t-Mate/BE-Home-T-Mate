package com.example.demo.repository;

import com.example.demo.model.Post;
import com.example.demo.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    void deleteById(Long postId);

    List<Post> findByUser(User user);
}
