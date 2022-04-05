package com.example.demo.repository;

import com.example.demo.model.Like;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);

////    @Query(  "select m from Comment m left join fetch m.post")
    Long countByPost(Post post);


//    @Query(  "select m from Like m left join fetch m.post")
    List<Like> findAllByPost(Post post);

}
