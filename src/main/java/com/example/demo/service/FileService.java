package com.example.demo.service;

import com.example.demo.config.S3Uploader;
import com.example.demo.dto.file.FileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FileService
{
    private final S3Uploader s3Uploader;

    public ResponseEntity<?> fileURL(List<MultipartFile> multipartFileList, MultipartFile videoFile) throws IOException
    {
        // 파일 혹은 비디오 파일을 넣지 않았을 경우 공백으로 처리.
        List<String> file = new ArrayList<>();
        String video = "";

        // 파일이 있을 경우 s3에 넣고 url 값을 받음.
        if ( multipartFileList != null)
        {
            for ( MultipartFile multipartFile : multipartFileList)
            {
                String fileUrl = s3Uploader.upload(multipartFile, "static");
                file.add(fileUrl);
            }
        }

        // 비디오가 있을 경우 s3에 넣고 url 값을 받음.
        if ( videoFile != null)
        {
            video = s3Uploader.upload(videoFile, "static");
        }

        FileResponseDto fileResponseDto = new FileResponseDto(file, video);

        return ResponseEntity.ok().body(fileResponseDto);
    }
}
