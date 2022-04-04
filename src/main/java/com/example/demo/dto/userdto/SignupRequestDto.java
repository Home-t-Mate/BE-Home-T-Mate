package com.example.demo.dto.userdto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequestDto
{
    private String username;

    private String nickname;

    private String password;
}
