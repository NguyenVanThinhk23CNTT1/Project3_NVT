package com.nvtdevmaster.lesson05.controller;

import com.nvtdevmaster.lesson05.entity.Info;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Devmaster :: Trang chủ");
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/services")
    public String services() {
        return "services";
    }


    @GetMapping("/profile")
    public String profile(Model model) {

        List<Info> profile = new ArrayList<>();
        profile.add(new Info(
                "Devmaster Academy",
                "dev",
                "contact@devmaster.edu.vn",
                "https://devmaster.edu.vn"
        ));

        model.addAttribute("nvtProfile", profile);

        return "profile";
    }
}
