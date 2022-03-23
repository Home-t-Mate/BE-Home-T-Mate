package com.example.demo.service;

import com.example.demo.dto.TodolistRequestDto;
import com.example.demo.model.User;
import com.example.demo.model.Todolist;
import com.example.demo.repository.TodolistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodolistService {

    private final TodolistRepository todolistRepository;

    //Todolist 추가하기
    public Todolist createlist(TodolistRequestDto requestDto, User user){
        Todolist todolist = new Todolist(requestDto, user);
        todolist.setTitle(requestDto.getTitle());
        todolist.setCompleted(requestDto.getCompleted());
        todolist.setStart(requestDto.getStart());
        todolist.setEnd(requestDto.getEnd());

        return todolistRepository.save(todolist);
    }

    //Todolist 수정하기
    public Todolist updatelist(Long id, TodolistRequestDto requestDto){
        Todolist todolist = todolistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


        if(requestDto.getTitle() != null){
            todolist.setTitle(requestDto.getTitle());
        }
        if(requestDto.getCompleted() != null){
            todolist.setCompleted(requestDto.getCompleted());
        }
        if(requestDto.getStart() != null){
            todolist.setStart(requestDto.getStart());
        }
        if(requestDto.getEnd() != null){
            todolist.setEnd(requestDto.getEnd());
        }
        return todolistRepository.save(todolist);
    }


    //Todolist 목록 전체 조회
    public List<Todolist> getlist(Long userId){
        return todolistRepository.findAllByUserId(userId);
    }


    //Todolist 삭제하기
    public void deletelist(Long id){
        todolistRepository.deleteById(id);
    }

}


