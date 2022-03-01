package com.example.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.dto.signupdto.SignupSocialDto;
import com.example.demo.dto.userdto.UserResponseDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.security.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RequiredArgsConstructor
@Service //서비스로 등록 -> Bean등록
public class KakaoUserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

//    @Value("${kakao.client.id}")
    private String clientId;

    @Transactional
    public SignupSocialDto kakaoLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
//        String accessToken = getAccessToken(code, "https://3.38.252.235/user/kakao/callback");
        String accessToken = getAccessToken(code, "http://localhost:3000/user/kakao/callback");

        // 2. 필요시에 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(accessToken);

        // 3. 로그인 JWT 토큰 발행
        String token = jwtTokenCreate(kakaoUser);

        return SignupSocialDto.builder()
                .token(token)
                .userId(kakaoUser.getId())
                .build();
    }

    @Transactional
    public UserResponseDto kakaoAddUserProfile(String code, Long userId) throws JsonProcessingException {
        // 업데이트 필요성 체크
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("유저조회불가")
        );

        UserResponseDto userLoginResponseDto;
        if (user.getAgeRange() == null) {
            // 1. "인가 코드"로 "액세스 토큰" 요청
//            String accessToken = getAccessToken(code, "https://3.38.252.235/user/kakao/callback/properties");
            String accessToken = getAccessToken(code, "http://localhost:3000/user/kakao/callback/properties");

            // 2. 유저 정보 업데이트
            userLoginResponseDto = updateUserProfile(accessToken, user);
        } else {
            userLoginResponseDto = UserResponseDto.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                    .profileImg(user.getProfileImg())
                    .gender(user.getGender())
                    .ageRange(user.getAgeRange())
                    .career(user.getCareer())
                    .phoneNum(user.getPhoneNum())
                    .selfIntro(user.getSelfIntro())
                    .certification(user.getPhoneNum() != null)
                    .build();
        }

        return userLoginResponseDto;
    }

    private String getAccessToken(String code, String redirect_uri) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "dbf70dbcc152160d45ec6ce156a6c37e");
//        body.add("redirect_uri", "http://3.38.252.235/user/kakao/callback");
        body.add("redirect_uri", "http://localhost:3000/user/kakao/callback");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    // 처음 로그인 시, 회원 가입 안되어 있으면 회원 가입 시켜주기
    private User registerKakaoUserIfNeeded(String accessToken) throws JsonProcessingException {
        JsonNode jsonNode = getKakaoUserInfo(accessToken);

        // DB 에 중복된 Kakao Id 가 있는지 확인
        String kakaoId = String.valueOf(jsonNode.get("id").asLong());
        User kakaoUser = userRepository.findByUsername(kakaoId).orElse(null);

        // 회원가입
        if (kakaoUser == null) {
            String kakaoNick = jsonNode.get("properties").get("nickname").asText();

            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            kakaoUser = new User(kakaoId, kakaoNick, encodedPassword);
            userRepository.save(kakaoUser);
        }

        return kakaoUser;
    }

    // 유저 프로필 등록 (나이대, 성별)
    private UserResponseDto updateUserProfile(String accessToken, User user) throws JsonProcessingException {
        JsonNode jsonNode = getKakaoUserInfo(accessToken);

        String ageRange = jsonNode.get("kakao_account").get("age_range").asText();
        String userAge = ageRange.split("~")[0];
        if (Integer.parseInt(userAge) >= 20){
            userAge += "대";
        } else {
            userAge = "청소년";
        }

        String gender = jsonNode.get("kakao_account").get("gender").asText();
        if(gender.equals("female")){
            gender = "여";
        } else {
            gender = "남";
        }

        user.updateKakaoProfile(userAge, gender);

        return UserResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .gender(user.getGender())
                .ageRange(user.getAgeRange())
                .certification(user.getPhoneNum() != null)
                .build();
    }

    // 카카오에서 동의 항목 가져오기
    private JsonNode getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(responseBody);
    }

    // JWT 토큰 생성
    private String jwtTokenCreate(User kakaoUser) {
        String TOKEN_TYPE = "BEARER";

        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails1 = ((UserDetailsImpl) authentication.getPrincipal());
        final String token = JwtTokenUtils.generateJwtToken(userDetails1);
        return TOKEN_TYPE + " " + token;
    }
}
