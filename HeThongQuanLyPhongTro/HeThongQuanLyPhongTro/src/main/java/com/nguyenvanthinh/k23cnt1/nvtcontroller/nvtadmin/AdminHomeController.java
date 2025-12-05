package com.nguyenvanthinh.k23cnt1.nvtcontroller.nvtadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    @GetMapping("/home")
    public String index() {
        return "admin/home";
    }
}

