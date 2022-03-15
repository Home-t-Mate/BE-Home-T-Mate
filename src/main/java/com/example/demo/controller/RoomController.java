package com.example.demo.controller;

import com.example.demo.dto.RoomPassRequestDto;
import com.example.demo.dto.RoomRequestDto;
import com.example.demo.dto.RoomResponseDto;
import com.example.demo.model.User;
import com.example.demo.model.Room;
import com.example.demo.repository.RedisRepository;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class RoomController {
    private final RoomService roomService;
    private final RedisRepository repository;


    //방 조회

    @GetMapping("/rooms")
    @ResponseBody
    public ResponseEntity<List<RoomResponseDto>> room() {
        return ResponseEntity.ok().body(roomService.getRooms());
    }

    //방 생성
    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity<Room> createRoom(@RequestBody RoomRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return ResponseEntity.ok().body(roomService.createRoom(requestDto, user));
    }
//
//    //디테일 페이지 진입입
//    //enter로 바꿔야함
   @PostMapping("/room/enter/{roomId}")
   @ResponseBody
    public RoomResponseDto enterRoom(@PathVariable String roomId, @RequestBody RoomPassRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
       System.out.println(requestDto);
        return roomService.enterRoom(roomId, requestDto, userDetails.getUser());
    }


   @GetMapping("/room/quit/{roomId}")
   @ResponseBody
   public void quitRoom(@PathVariable String roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//       Room room = roomRepository.findById(roomId).orElseThrow(()-> new IllegalArgumentException("해당 방이 존재하지 않습니다."));
//       this.mapSessions.put(room.getName(), this.mapSessions.get(room.getName()) - 1);
}

//특정방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<RoomResponseDto> getRoomDetail(@RequestBody RoomRequestDto requestDto) {
        return ResponseEntity.ok().body(roomService.getRoom(requestDto));
    }

    @PutMapping("/room/workout")
    public void workout(@RequestBody RoomRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        roomService.workout(requestDto);
    }


    @PostMapping("/user")
    @ResponseBody
    public Optional<User> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return Optional.ofNullable(user);
    }
}
