package com.example.userservice.security;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

//인증 작업을 처리 해주기 위해 사용
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private UserService userService;
    private Environment env;
    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserService userService, Environment env) {
        super(authenticationManager);
        this.userService = userService;
        this.env = env;
    }

    //login되면 가장 먼저 호출되는 메소드(1)
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        
        try {
            //request에서 username과 password를 추출
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);
            System.out.print("호출..............................");
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
        String userName = ((User)authResult.getPrincipal()).getUsername();
        UserDto userDto = userService.getUserDetailByEmail(userName);

        //yml 파일에 저장된 token.expriation_time을 현재 날짜에 더해주고, signature 사용 알고리즘은 HS512, token.secret 사용
        String token = Jwts.builder()
                        .setSubject(userDto.getUserId())
                        .setExpiration(new Date(System.currentTimeMillis() +
                                Long.parseLong(env.getProperty("token.expiration_time"))))
                        .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                        .compact();
        response.addHeader("token", token);
        response.addHeader("userId", userDto.getUserId());
        //System.out.println(("userName : "+((User)authResult.getPrincipal()).getUsername()));
    }
}
