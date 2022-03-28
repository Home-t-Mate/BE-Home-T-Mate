package com.example.demo.repository;

import com.example.demo.model.Post;
import com.example.demo.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByPost(Post post);

    Long deleteByPost(Post post);

    List<Photo> findAllByPost(Post post);
}
