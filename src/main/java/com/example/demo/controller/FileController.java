package com.example.demo.controller;


import com.example.demo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class FileController
{
    private final FileService fileService;

    // 사진 선택
    @PostMapping("/api/image")
    public ResponseEntity<?> fileURL(@RequestPart(name = "file", required = false) List<MultipartFile> multipartFileList,
                                     @RequestPart(name = "video",required = false) MultipartFile videoFile) throws IOException {
       return fileService.fileURL(multipartFileList,videoFile);
    }
}
