package com.example.userservice.security;

import com.example.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//설정파일로 등록
@Configuration
//Security 담당 등록
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;

    public WebSecurity(Environment env, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.env = env;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //권한과 관련된 설정
        http.csrf().disable();
        //users로 들어온 모든 요청이 들어오면 권한을 모두 부여
//        http.authorizeRequests().antMatchers("/users/**").permitAll();
//        http.authorizeRequests().antMatchers("/login").permitAll();
//        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
//        http.authorizeRequests().antMatchers("/error/**").permitAll();

        http.authorizeRequests().antMatchers("/**")
                        .hasIpAddress("127.0.0.1")
                        .and()
                        .addFilter(getAuthenticationFilter());

        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter()throws Exception{
        AuthenticationFilter authenticationFilter =
                new AuthenticationFilter(authenticationManager(), userService, env);
        //AthenticationManager 등록
//        authenticationFilter.setAuthenticationManager(authenticationManager());

        return authenticationFilter;
    }
}
