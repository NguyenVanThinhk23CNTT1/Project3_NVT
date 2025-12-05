package com.nguyenvanthinh.k23cnt1.nvtcontroller.nvtadmin;

import com.nguyenvanthinh.k23cnt1.nvtentity.TenantRequest;
import com.nguyenvanthinh.k23cnt1.nvtservice.TenantRequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/requests")
public class AdminRequestController {

    private final TenantRequestService requestService;

    @GetMapping
    public String list(Model model, HttpServletRequest request) {
        model.addAttribute("requests", requestService.getAll());
        model.addAttribute("currentPath", request.getRequestURI());
        return "admin/request/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, HttpServletRequest request) {
        model.addAttribute("req", requestService.getById(id));
        model.addAttribute("currentPath", request.getRequestURI());
        return "admin/request/detail";
    }

    @GetMapping("/approve/{id}")
    public String approve(@PathVariable Long id) {
        requestService.approve(id);
        return "redirect:/admin/requests";
    }

    @PostMapping("/reject/{id}")
    public String reject(@PathVariable Long id, @RequestParam String reason) {
        requestService.reject(id, reason);
        return "redirect:/admin/requests";
    }
}
