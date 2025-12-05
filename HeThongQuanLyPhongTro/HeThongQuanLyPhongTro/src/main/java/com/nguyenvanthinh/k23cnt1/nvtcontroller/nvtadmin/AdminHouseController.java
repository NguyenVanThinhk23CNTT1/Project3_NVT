package com.nguyenvanthinh.k23cnt1.nvtcontroller.nvtadmin;

import com.nguyenvanthinh.k23cnt1.nvtentity.House;
import com.nguyenvanthinh.k23cnt1.nvtservice.HouseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/houses")
public class AdminHouseController {

    private final HouseService houseService;

    @GetMapping
    public String list(Model model, HttpServletRequest request) {
        model.addAttribute("houses", houseService.getAll());
        model.addAttribute("currentPath", request.getRequestURI());
        return "admin/house/list";
    }

    @GetMapping("/add")
    public String add(Model model, HttpServletRequest request) {
        model.addAttribute("house", new House());
        model.addAttribute("currentPath", request.getRequestURI());
        return "admin/house/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, HttpServletRequest request) {
        model.addAttribute("house", houseService.getById(id));
        model.addAttribute("currentPath", request.getRequestURI());
        return "admin/house/form";
    }

    @PostMapping("/save")
    public String save(House house) {
        houseService.save(house);
        return "redirect:/admin/houses";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        houseService.delete(id);
        return "redirect:/admin/houses";
    }
}
