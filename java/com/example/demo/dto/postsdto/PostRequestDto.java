package com.example.demo.dto.postsdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {
//    private String title;
    private String content;
    private String postImg;
//    private List<Long> postImgId;
}
