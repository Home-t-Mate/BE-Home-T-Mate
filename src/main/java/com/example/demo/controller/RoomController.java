package com.example.demo.controller;

import com.example.demo.dto.*;
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

//  방 조회 페이지 처리(무한스크롤)
    @GetMapping("/roomsscroll")
    @ResponseBody
    public ResponseEntity<List<RoomResponseDto>> roomscrooll(@RequestParam("page") int page,
                                                             @RequestParam("size") int size

    ) {
        page = page - 1;
        return ResponseEntity.ok().body(roomService.roomscroll(page, size));

    }


    //방 생성
    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody RoomRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return ResponseEntity.ok().body(roomService.createRoom(requestDto, user));
    }

//  userEnter 테이블 조인(현재 방에 접속 중인 유저 확인 테이블)
   @PostMapping("/room/enter/{roomId}")
   @ResponseBody
    public ResponseEntity<List<EnterUserResponseDto>> enterRoom(@PathVariable String roomId,
                                                                @RequestBody RoomPassRequestDto requestDto,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(roomService.enterRoom(roomId, requestDto, userDetails.getUser()));
    }



//    enterUser 삭제
   @DeleteMapping("/room/quit/{roomId}")
   @ResponseBody
   public void quitRoom(@PathVariable String roomId,
                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
       roomService.quitRoom(roomId, user);
   }

//특정방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<RoomResponseDto> getRoomDetail(@RequestBody RoomRequestDto requestDto) {
        return ResponseEntity.ok().body(roomService.getRoom(requestDto));
    }

//    운동중 true, 종료시 false
    @PutMapping("/room/workout")
    public void workout(@RequestBody RoomRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        roomService.workout(requestDto);
    }

//    room 삭제
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
