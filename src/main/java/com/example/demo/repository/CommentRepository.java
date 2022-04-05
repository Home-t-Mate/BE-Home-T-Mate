package com.example.demo.repository;

import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>
{
//    @Query(  "select m from Comment m left join fetch m.post")
//    List<Comment> findAllByPost(Post post);

    List<Comment> findByPostIdOrderByModifiedAtDesc(Long postId);
}