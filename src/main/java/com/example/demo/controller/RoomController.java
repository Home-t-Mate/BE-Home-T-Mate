package com.example.demo.controller;

import com.example.demo.dto.RoomCheckRequestDto;
import com.example.demo.dto.RoomPassRequestDto;
import com.example.demo.dto.RoomRequestDto;
import com.example.demo.dto.RoomResponseDto;
import com.example.demo.model.User;
import com.example.demo.model.Room;
import com.example.demo.repository.RedisRepository;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @GetMapping("/roomsscroll")
    @ResponseBody
    public ResponseEntity<List<RoomResponseDto>> roomscrooll(@RequestParam("page") int page,
                                             @RequestParam("size") int size,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails

    ) {
        User user = userDetails.getUser();
        page = page - 1;
//        return roomService.roomscroll(user, page, size, sortBy, isAsc);
        return ResponseEntity.ok().body(roomService.roomscroll(user, page, size));

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
    public ResponseEntity<RoomResponseDto> enterRoom(@PathVariable String roomId, @RequestBody RoomPassRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
       System.out.println(requestDto);
       System.out.println(requestDto.getPassword());
        return ResponseEntity.ok().body(roomService.enterRoom(roomId, requestDto, userDetails.getUser()));
    }



   @DeleteMapping("/room/quit/{roomId}")
   @ResponseBody
   public void quitRoom(@PathVariable String roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
       roomService.quitRoom(roomId, user);
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

    @DeleteMapping("/room/delete/{roomId}")
    public void deleteRoom(@RequestParam String roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        roomService.deleteRoom(roomId, user);
    }


    //룸 이름 조회
    @PostMapping("/room/roomcheck")
    @ResponseBody
    public ResponseEntity<Boolean> roomCheck(@RequestBody RoomCheckRequestDto roomCheckRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return ResponseEntity.ok().body(roomService.roomCheck(roomCheckRequestDto, user));
    }

    @PostMapping("/user")
    @ResponseBody
    public Optional<User> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return Optional.ofNullable(user);
    }
}
