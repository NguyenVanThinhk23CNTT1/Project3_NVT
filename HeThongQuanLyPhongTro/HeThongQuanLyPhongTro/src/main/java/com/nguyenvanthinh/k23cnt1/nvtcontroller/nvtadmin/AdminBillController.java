package com.nguyenvanthinh.k23cnt1.nvtcontroller.nvtadmin;

import com.nguyenvanthinh.k23cnt1.nvtservice.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/bills")
public class AdminBillController {

    private final BillService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("bills", service.getAll());
        return "admin/bill/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("bill", service.getById(id));
        return "admin/bill/detail";
    }
}
