package com.example.demo.service;

import com.example.demo.config.S3Uploader;
import com.example.demo.dto.userdto.UserProfileUpdateDto;
import com.example.demo.dto.userdto.UserResponseDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;
    private final String profileImgDirName = "Profile";
    private final String defaultImg = "https://hometmate.s3.ap-northeast-2.amazonaws.com/static/profile.png";

    //유저 프로필 조회
    @Transactional
    public UserResponseDto getUserProfile(User user){
        return generateUserResponseDto(user);
    }

    //유저 프로필 수정
    @Transactional
    public UserResponseDto updateUserProfile(MultipartFile profileImg,
                                             User user) {
        User dbUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));


        //프로필 이미지 저장 , 저장 경로 업데이트
        if(profileImg != null){
            //빈 이미지가 아닐때만 기존 이미지 삭제
            if(!dbUser.getProfileImg().equals(defaultImg)){
                try{
                    String source = URLDecoder.decode(dbUser.getProfileImg().replace("https://homehang.s3.ap-northeast-2.amazonaws.com/",""),"UTF-8");
                    s3Uploader.deleteFromS3(source);
                } catch (Exception ignored) {
                }
            }

            if(!profileImg.getOriginalFilename().equals("delete")){
                try{
                    String profileImgUrl = s3Uploader.upload(profileImg, profileImgDirName);
                    dbUser.setProfileImg(profileImgUrl);
                } catch (Exception e) {
                    dbUser.setProfileImg(defaultImg);
                }
            } else {
                dbUser.setProfileImg(defaultImg);
            }
        }
        return generateUserResponseDto(dbUser);
    }

    private UserResponseDto generateUserResponseDto(User user) {
        return UserResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .build();
    }
}
