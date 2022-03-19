package com.example.demo.repository;

import com.example.demo.model.Todolist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodolistRepository extends JpaRepository<Todolist,Long> {
    List<Todolist>findAllByUserId (Long userId);
}

