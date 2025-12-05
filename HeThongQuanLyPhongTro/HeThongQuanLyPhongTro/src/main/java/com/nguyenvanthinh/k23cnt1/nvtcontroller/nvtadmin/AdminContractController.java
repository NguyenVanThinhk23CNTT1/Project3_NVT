package com.nguyenvanthinh.k23cnt1.nvtcontroller.nvtadmin;

import com.nguyenvanthinh.k23cnt1.nvtservice.ContractService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/contracts")
public class AdminContractController {

    private final ContractService contractService;

    @GetMapping
    public String list(Model model, HttpServletRequest request) {
        model.addAttribute("contracts", contractService.getAll());
        model.addAttribute("currentPath", request.getRequestURI());
        return "admin/contract/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, HttpServletRequest request) {
        model.addAttribute("c", contractService.getById(id));
        model.addAttribute("currentPath", request.getRequestURI());
        return "admin/contract/detail";
    }
}
