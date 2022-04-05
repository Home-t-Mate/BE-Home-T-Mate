package com.example.demo.repository;

import com.example.demo.model.Post;
import com.example.demo.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
//    @Query(  "select m from Photo m left join fetch m.post")
    List<Photo> findByPost(Post post);

    Long deleteByPost(Post post);

    List<Photo> findAllByPost(Post post);
}
