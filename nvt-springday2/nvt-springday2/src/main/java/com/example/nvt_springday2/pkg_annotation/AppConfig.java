package com.example.nvt_springday2.pkg_annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AppConfig {
    @Bean
    public String appName() {
        return "<h1> Nguyễn Văn Thịnh </h1><h2>Spring Boot Application";
    }
}