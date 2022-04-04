package com.example.demo.controller;

import com.example.demo.dto.TodolistRequestDto;
import com.example.demo.model.User;
import com.example.demo.model.Todolist;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.service.TodolistService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodolistController {

    private final TodolistService todolistService;

    @Autowired
    public TodolistController(TodolistService todolistService){
        this.todolistService = todolistService;
    }

    //Todolist 등록하기
    @PostMapping("/api/todolist")
    public ResponseEntity<Todolist> createlist(@RequestBody TodolistRequestDto requestDto,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return ResponseEntity.ok().body(todolistService.createlist(requestDto,user));
    }

    //Todolist 수정하기
    @PutMapping("/api/todolist/{id}")
    public ResponseEntity<Long> updatelist(@PathVariable Long id,
                                           @RequestBody TodolistRequestDto requestDto){

        Todolist todolist = todolistService.updatelist(id,requestDto);
        return ResponseEntity.ok().body(todolist.getId());
    }

    //Todolist 조회하기
    @GetMapping("/api/todolist")
    @ResponseBody
    public ResponseEntity<List<Todolist>> getlist (@AuthenticationPrincipal UserDetailsImpl userDetails){
        Long userId = userDetails.getUser().getId();

        return ResponseEntity.ok().body(todolistService.getlist(userId));
    }

    //Todolist 삭제하기
    @DeleteMapping("/api/todolist/{id}")
    public ResponseEntity<Long> deletelist(@PathVariable Long id){
        todolistService.deletelist(id);
        return ResponseEntity.ok().body(id);
    }
}

