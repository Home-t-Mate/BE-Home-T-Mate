package com.example.demo.dto.signupdto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupSmsCertificationDto{
    private String phoneNumber;
    private String randomNumber;
}