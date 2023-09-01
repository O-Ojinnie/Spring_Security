package com.example.userservice.security;

import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    //인증 작업을 처리 해주기 위해 사용

    //인증을 호출해주는


    //login되면 가장 먼저 호출되는 메소드(1)
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        
        try {
            //request에서 username과 password를 추출
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            //(3)
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            //(2)
                            //토큰으로 AuthenticationManager에 전달해야하므로
                            //Token의 형태로 변환해서 반환
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>() //권한이 담길 리스트
                    )
            );
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
    }
}
