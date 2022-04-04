package com.example.demo.model;

import com.example.demo.dto.userdto.UserProfileUpdateDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
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

    @Column
    private String profileImg;


    @Column
    private String email;


//    // 카카오 유저
//    public User(String username, String nickname, String enPassword){
//        this.username = username;
//        this.nickname = nickname;
//        this.password = enPassword;
//        this.profileImg = "https://skifriendbucket.s3.ap-northeast-2.amazonaws.com/static/defalt+user+frofile.png";
//    }

    public User(String username, String nickname, String enPassword, String profileImg, String email){
        this.username = username;
        this.nickname = nickname;
        this.password = enPassword;
        this.profileImg = profileImg;
        this.email = email;
    }

    @Builder
    public User(String username, String nickname, String enPassword, String profileImg){
        this.username = username;
        this.nickname = nickname;
        this.password = enPassword;
        this.profileImg = profileImg;
    }

    public void setProfileImg(String imgPath) {
        this.profileImg = imgPath;
    }
}
