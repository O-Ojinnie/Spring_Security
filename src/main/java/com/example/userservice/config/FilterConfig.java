package com.example.userservice.config;

import com.example.userservice.Filter.MyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class FilterConfig {

    @Autowired
    private Environment env;

    @Bean
    public FilterRegistrationBean<MyFilter> filter(){
        FilterRegistrationBean<MyFilter> bean =
            new FilterRegistrationBean<>(new MyFilter(env));

        bean.addUrlPatterns("/user2/*");
        bean.setOrder(0); //MyFilter가 1순위가 되도록 설정
        
        return bean;
    }
}
