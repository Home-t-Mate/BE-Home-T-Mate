package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.Room;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final RoomService roomService;
    private final UserRepository userRepository;
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
    public Room createRoom(@RequestBody String name, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
    return roomService.createRoom(name, user);
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

    @PostMapping("/user")
    @ResponseBody
    public Optional<User> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        return user;
    }
}
