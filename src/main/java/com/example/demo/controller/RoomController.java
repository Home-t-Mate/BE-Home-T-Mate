package com.example.demo.controller;

import com.example.demo.dto.RoomDto;
import com.example.demo.model.User;
import com.example.demo.model.Room;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class RoomController {
    private final RoomRepository roomRepository;
    private final RoomService roomService;
    private final UserRepository userRepository;
    private final int LIMIT = 2;

    private Map<String, Integer> mapSessions = new ConcurrentHashMap<>();



    //방 조회
    @GetMapping("/rooms")
    @ResponseBody
    public List<Room> room() {
        return roomRepository.findAll();
    }

    //방 생성
    @PostMapping("/room")
    @ResponseBody
    public Room createRoom(@RequestBody RoomDto roomDto, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        this.mapSessions.put(roomDto.getName(), 1);
        return roomService.createRoom(roomDto, user);
    }

    //디테일 페이지 진입입
   @GetMapping("/room/join/{roomId}")
   @ResponseBody
    public void roomDetail(@PathVariable Long roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Room room = roomRepository.findById(roomId).orElseThrow(()-> new IllegalArgumentException("해당 방이 존재하지 않습니다."));
       System.out.println(room.getName());
   System.out.println(mapSessions.get(room.getName()));
        if (mapSessions.get(room.getName()) > LIMIT) {
            System.out.println("진입");
            throw new IllegalArgumentException("입장인원을 초과하였습니다.");
        } else {
            this.mapSessions.put(room.getName(), this.mapSessions.get(room.getName()) + 1);
        }
   }



   @GetMapping("/room/quit/{roomId}")
   @ResponseBody
   public void quitRoom(@PathVariable Long roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
       Room room = roomRepository.findById(roomId).orElseThrow(()-> new IllegalArgumentException("해당 방이 존재하지 않습니다."));
       this.mapSessions.put(room.getName(), this.mapSessions.get(room.getName()) - 1);
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
