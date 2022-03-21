package com.example.demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.dto.userdto.UserLoginResponseDto;
import com.example.demo.model.User;
import com.example.demo.security.jwt.JwtTokenUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "BEARER";

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {
        final UserDetailsImpl userDetails = ((UserDetailsImpl) authentication.getPrincipal());

        // Token 생성
        final String token = JwtTokenUtils.generateJwtToken(userDetails);
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);

        //UserId, Nickname 내려주기
        response.setContentType("application/json");
        User user = userDetails.getUser();
        UserLoginResponseDto responseDto = UserLoginResponseDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
//                .isProfile(user.getAgeRange() != null)
                .build();

        String result = mapper.writeValueAsString(responseDto);
        response.getWriter().write(result);
    }
}
