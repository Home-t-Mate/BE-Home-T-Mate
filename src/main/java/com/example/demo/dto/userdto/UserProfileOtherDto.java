package com.example.demo.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileOtherDto {
    private String nickname;
    private String profileImg;
    private String gender;
    private String ageRange;
    private String career;
    private String selfIntro;
}