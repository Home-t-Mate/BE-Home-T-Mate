package com.example.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
//import com.example.demo.certification.MessageService;
//import com.example.demo.dto.signupdto.SignupPhoneNumDto;
//import com.example.demo.dto.signupdto.SignupSmsCertificationDto;
import com.example.demo.dto.signupdto.SignupSocialDto;
import com.example.demo.dto.userdto.UserResponseDto;
//import com.example.demo.entity.User;
//import com.example.demo.security.UserDetailsImpl;
import com.example.demo.service.KakaoUserService;
//import com.example.demo.service.NaverUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class SignupController {
    private final KakaoUserService kakaoUserService;
    private final String AUTH_HEADER = "Authorization";


    // 카카오 회원가입
    @GetMapping("/user/kakao/callback")
    public Long kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        // authorizedCode: 카카오 서버로부터 받은 인가 코드
        SignupSocialDto signupKakaoDto = kakaoUserService.kakaoLogin(code);
        response.addHeader(AUTH_HEADER, signupKakaoDto.getToken());

        return signupKakaoDto.getUserId();
    }

    // 카카오 프로필 업데이트
    @GetMapping("/user/kakao/callback/{userId}")
    public ResponseEntity<UserResponseDto> kakaoAddUserProfile(@RequestParam String code,
                                                              @PathVariable Long userId
    ) throws IOException {

        return ResponseEntity.ok().body(kakaoUserService.kakaoAddUserProfile(code, userId));
    }
}