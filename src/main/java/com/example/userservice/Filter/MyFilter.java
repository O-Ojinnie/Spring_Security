package com.example.userservice.Filter;

import io.jsonwebtoken.Jwts;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyFilter implements Filter {

    private Environment env;

    public MyFilter(Environment env) {
        this.env = env;
    }

    @Override
    public void doFilter(ServletRequest request2,
                         ServletResponse response2,
                         FilterChain chain) throws IOException, ServletException {
        System.out.println("필터 호출");

        HttpServletRequest request = (HttpServletRequest) request2;
        HttpServletResponse response = (HttpServletResponse) response2;

        if(request.getHeader("AUTHORIZATION") == null){
            onError(response, "UNAUTHORIZATION");
        }else{
            String authorizationHeader = request.getHeader("AUTHORIZATION");
            //JWT에는 Bearer이라는 문자가 붙어있기 때문에 순수 JWT Token을 받기 위해 Bearer 문자열 제거
            String jwt = authorizationHeader.replace("Bearer", "");

            if(!isJwtValid(jwt)){
                onError(response,"UNAUTHORIZATION");
            }
        }
        chain.doFilter(request2,response2);
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null;

        try {
            subject = Jwts.parser().setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(jwt).getBody().getSubject();

        }catch (Exception e){
            returnValue = false;
        }

        if(subject == null || subject.isEmpty()){
            returnValue = false;
        }

        return returnValue;
    }

    private void onError(HttpServletResponse response, String httpStatus) throws IOException{
        response.addHeader("error", httpStatus);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, httpStatus);
    }
}
