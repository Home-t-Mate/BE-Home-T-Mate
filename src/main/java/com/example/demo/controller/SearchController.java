package com.example.demo.controller;


import com.example.demo.dto.RoomResponseDto;
import com.example.demo.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/room/search")
    @ResponseBody
    public List<RoomResponseDto> search(@RequestParam(value = "keyword") String keyword) {
        return searchService.search(keyword);
    }
}
