package com.example.demo.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 유저 프로필 조회, 수정, 작성
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto{
    private Long userId;
  
//    private String phoneNum;
    private String nickname;
    private String profileImg;
//    private String gender;
//    private String ageRange;
//    private String career;
//    private String selfIntro;
//    private String email;
//    private boolean certification;

}