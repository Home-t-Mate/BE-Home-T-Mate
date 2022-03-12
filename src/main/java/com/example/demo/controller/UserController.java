package com.example.demo.controller;

import com.example.demo.dto.userdto.UserProfileUpdateDto;
import com.example.demo.dto.userdto.UserResponseDto;
import com.example.demo.model.User;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //사용자 정보 조회
    @GetMapping("/user/info")
    public ResponseEntity<UserResponseDto> getUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return ResponseEntity.ok().body(userService.getUserProfile(user));
    }

    //사용자 정보 수정하기
    @PutMapping("/user/info")
    public ResponseEntity<UserResponseDto> updateUserProfile(@RequestPart(value = "profileImg", required = false)MultipartFile profileImg,
                                                             @RequestPart(value = "requestDto") UserProfileUpdateDto requestDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        UserResponseDto userResponseDto = userService.updateUserProfile(profileImg, requestDto, user);
        return ResponseEntity.ok().body(userResponseDto);

    }
}