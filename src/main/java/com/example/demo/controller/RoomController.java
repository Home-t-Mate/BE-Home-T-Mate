package com.example.demo.controller;

import com.example.demo.model.LoginInfo;
import com.example.demo.model.Room;
import com.example.demo.repository.RoomRepository;
import com.example.demo.service.JwtTokenProvider;
import com.example.demo.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class RoomController {
    private final RoomRepository roomRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoomService roomService;

    @GetMapping("/room")
    public String rooms() {return "/chat/room";}

    //방 조회
    @GetMapping("/rooms")
    @ResponseBody
    public List<Room> room() {
        return roomRepository.findAll();
    }

    //방 생성
    @PostMapping("/room")
    @ResponseBody
    public Room createRoom(@RequestParam String name) {
        System.out.println(name);
    return roomService.createRoom(name);
    }


    //디테일 페이지 진입입
   @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable Long roomId) {
        model.addAttribute("roomId", roomId);
        return  "/chat/roomdetail";
    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public Optional<Room> roomInfo(@PathVariable Long roomId) {
        return roomService.findRoom(roomId);
    }

    @GetMapping("/user")
    @ResponseBody
    public LoginInfo getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        return LoginInfo.builder().name(name).token(jwtTokenProvider.generateToken(name)).build();
    }
}
