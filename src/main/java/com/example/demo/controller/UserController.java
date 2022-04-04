package com.example.demo.controller;

import com.example.demo.dto.userdto.LoginRequestDto;
import com.example.demo.dto.userdto.SignupRequestDto;
import com.example.demo.dto.userdto.UserProfileUpdateDto;
import com.example.demo.dto.userdto.UserResponseDto;
import com.example.demo.model.User;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //사용자 정보 조회
    @GetMapping("/api/user/info")
    public ResponseEntity<UserResponseDto> getUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return ResponseEntity.ok().body(userService.getUserProfile(user));
    }

    //사용자 정보 수정하기
    @PutMapping("/api/user/info")
    public ResponseEntity<UserResponseDto> updateUserProfile(@RequestPart(value = "profileImg", required = false)MultipartFile profileImg,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
        UserResponseDto userResponseDto = userService.updateUserProfile(profileImg, user);
        return ResponseEntity.ok().body(userResponseDto);
    }


//
//    /* 로그인 */
//    @PostMapping("/api/user/login")
//    public LoginResultDto userLogin(@RequestBody LoginRequestDto loginRequestDto,
//                                    HttpServletResponse response){
//        return userService.userLogin(loginRequestDto, response);
//    }
}