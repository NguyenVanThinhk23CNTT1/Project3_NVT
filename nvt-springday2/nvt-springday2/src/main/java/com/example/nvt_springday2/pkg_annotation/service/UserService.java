package com.example.nvt_springday2.pkg_annotation.service;


import org.springframework.stereotype.Service;
@Service
public class UserService {
    public String getUserDetails() {
        return "<h1>User details";
    }
}