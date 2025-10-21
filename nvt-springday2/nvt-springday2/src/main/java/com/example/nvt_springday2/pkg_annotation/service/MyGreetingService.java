package com.example.nvt_springday2.pkg_annotation.service;

import org.springframework.stereotype.Service;

@Service
public class MyGreetingService {
    public String greet(){
        return "<h1>Hello from MyGreetingService</h1>";
    }
}