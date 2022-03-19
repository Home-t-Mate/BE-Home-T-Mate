package com.example.demo.repository;

import com.example.demo.model.Post;
import com.example.demo.model.Poto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PotoRepository extends JpaRepository<Poto, Long> {
    List<Poto> findByPost(Post post);
}
