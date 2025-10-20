package com.example.welcome;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Welcome Page");
        model.addAttribute("message", "Welcome to My Spring Boot Website!");
        return "welcome"; // -> tương ứng với welcome.html trong thư mục templates
    }
}
