package com.example.demo.service;

import com.example.demo.dto.TodolistRequestDto;
import com.example.demo.entity.User;
import com.example.demo.model.Todolist;
import com.example.demo.repository.TodolistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service

public class TodolistService {

    private final TodolistRepository todolistRepository;

    @Autowired
    public TodolistService(TodolistRepository todolistRepository){

        this.todolistRepository = todolistRepository;
    }

    //Todolist 추가하기
    public Todolist createlist(TodolistRequestDto requestDto, User user){
        Todolist todolist = new Todolist(requestDto, user);
        todolist.setTitle(requestDto.getTitle());
        todolist.setCompleted(requestDto.getCompleted());
        todolist.setStartAt(requestDto.getStartAt());
        todolist.setEndAt(requestDto.getEndAt());

        return this.todolistRepository.save(todolist);
    }

    //Todolist 수정하기
    public Todolist updatelist(Long id, TodolistRequestDto requestDto){
        Todolist todolist = this.searchById(id);
        if(requestDto.getTitle() != null){
            todolist.setTitle(requestDto.getTitle());
        }
        if(requestDto.getCompleted() != null){
            todolist.setCompleted(requestDto.getCompleted());
        }
        if(requestDto.getStartAt() == null){
            todolist.setStartAt(requestDto.getStartAt());
        }
        if(requestDto.getEndAt() == null){
            todolist.setEndAt(requestDto.getEndAt());
        }
        return this.todolistRepository.save(todolist);
    }

    //Todolist 목록 중 한개 조회
    public Todolist searchById(Long id){
        return this.todolistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    //Todolist 목록 전체 조회
    public List<Todolist> getlist(Long userId){

        return this.todolistRepository.findAllByUserId(userId);
    }


    //Todolist 삭제하기
    public void deletelist(Long id){

        this.todolistRepository.deleteById(id);
    }

}


