package com.example.demo.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponseDto{
    private Long userId;
    private String nickname;
    private boolean isProfile;
}