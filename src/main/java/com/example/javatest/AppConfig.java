package com.example.javatest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.handler.MappedInterceptor;
import com.example.javatest.interceptors.ArenaInterceptor;
import com.example.javatest.service.ArenaService;

@Configuration
public class AppConfig {

    @Bean
    public MappedInterceptor loginInter() {
        return new MappedInterceptor(null, new ArenaInterceptor());
    }
}
