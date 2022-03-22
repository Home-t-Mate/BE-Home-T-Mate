package com.example.demo.dto.signupdto;

import com.example.demo.dto.userdto.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupSocialDto {
    String token;
    Long userId;
    UserResponseDto userResponseDto;
}

