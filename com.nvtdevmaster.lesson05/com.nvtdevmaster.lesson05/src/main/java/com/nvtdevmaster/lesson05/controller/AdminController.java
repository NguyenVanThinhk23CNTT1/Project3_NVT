package com.nvtdevmaster.lesson05.controller;

import com.nvtdevmaster.lesson05.entity.Info;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    List<Info> list = new ArrayList<>(
            List.of(
                    new Info("Devmaster Academy", "dev", "contact@devmaster.edu.vn", "https://devmaster.edu.vn"),
                    new Info("Nguyễn Văn Thịnh", "thinh", "thinh@gmail.com", "https://example.com")
            )
    );

    @GetMapping("/info")
    public String infoList(Model model) {
        model.addAttribute("infos", list);
        return "admin/info-list";
    }

    @GetMapping("/info/add")
    public String addInfo() {
        return "admin/info-add";
    }

    @PostMapping("/info/save")
    public String saveInfo(Info info) {
        list.add(info);
        return "redirect:/admin/info";
    }

    @GetMapping("/info/edit/{nickname}")
    public String editInfo(@PathVariable String nickname, Model model) {
        Info found = list.stream()
                .filter(x -> x.getNickname().equals(nickname))
                .findFirst()
                .orElse(null);

        model.addAttribute("info", found);
        return "admin/info-edit";
    }

    @PostMapping("/info/update")
    public String updateInfo(Info info) {
        list.removeIf(x -> x.getNickname().equals(info.getNickname()));
        list.add(info);
        return "redirect:/admin/info";
    }

    @GetMapping("/info/delete/{nickname}")
    public String deleteInfo(@PathVariable String nickname) {
        list.removeIf(x -> x.getNickname().equals(nickname));
        return "redirect:/admin/info";
    }
}
