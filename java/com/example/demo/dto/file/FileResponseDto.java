package com.example.demo.dto.file;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FileResponseDto
{
    private List<String> file;
//    private String video;

    public FileResponseDto(List<String> file)
    {
        this.file = file;
//        this.video = video;
    }
}
