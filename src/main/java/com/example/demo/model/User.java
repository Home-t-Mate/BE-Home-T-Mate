package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false) // 닉네임 중복 허용
    private String nickname;

    @Column(nullable = false)
    private String password;

//    @Column
//    private String phoneNum;

    @Column
    private String userImg;

//    @Column
//    private String gender;

//    @Column
//    private String ageRange;

//    @Column
//    private String career;

//    @Column
//    private String selfIntro;

    // 일반 유저
//    public User(SignupRequestDto requestDto, String enPassword) {
//        this.username = requestDto.getUsername();
//        this.nickname = requestDto.getNickname();
//        this.phoneNum = requestDto.getPhoneNum();
//        this.password = enPassword;
//        this.profileImg = "https://skifriendbucket.s3.ap-northeast-2.amazonaws.com/static/defalt+user+frofile.png";
//    }

    // 카카오 유저
    public User(String username, String nickname, String Password){
        this.username = username;
        this.nickname = nickname;
        this.password = Password;
//        this.userImg = "https://skifriendbucket.s3.ap-northeast-2.amazonaws.com/static/defalt+user+frofile.png";
    }

//    public void updateKakaoProfile(String ageRange, String gender) {
//        this.ageRange = ageRange;
//        this.gender = gender;
//    }

//    // 네이버 유저
//    public User(String username, String nickname, String enPassword, String gender, String ageRange){
//        this.username = username;
//        this.nickname = nickname;
//        this.gender = gender;
//        this.ageRange = ageRange;
//        this.password = enPassword;
//        this.profileImg = "https://skifriendbucket.s3.ap-northeast-2.amazonaws.com/static/defalt+user+frofile.png";
//    }
//
//    public void setProfileImg(String imgPath) {
//        this.profileImg = imgPath;
//    }
//
//    public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }
//
////    public void updatePassword(String enPassword) {
////        this.password = enPassword;
////    }
//
////    public void createUserProfile(UserProfileRequestDto requestDto) {
////        this.gender = requestDto.getGender();
////        this.ageRange = requestDto.getAgeRange();
////        this.career = requestDto.getCareer();
////        this.selfIntro = requestDto.getSelfIntro();
////    }
//
//    public void update(UserProfileUpdateDto requestDto) {
//        this.nickname = requestDto.getNickname();
//        this.career = requestDto.getCareer();
//        this.selfIntro = requestDto.getSelfIntro();
//    }
}
