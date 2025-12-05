package com.nguyenvanthinh.k23cnt1.nvtcontroller.nvtadmin;

import com.nguyenvanthinh.k23cnt1.nvtentity.PaymentInfo;
import com.nguyenvanthinh.k23cnt1.nvtservice.PaymentInfoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/payment")
public class AdminPaymentController {

    private final PaymentInfoService paymentService;

    @GetMapping
    public String config(Model model, HttpServletRequest request) {
        PaymentInfo config = paymentService.get();
        model.addAttribute("payment", config);
        model.addAttribute("currentPath", request.getRequestURI());
        return "admin/payment/form";
    }

    @PostMapping("/save")
    public String save(PaymentInfo paymentConfig) {
        paymentService.save(paymentConfig);
        return "redirect:/admin/payment";
    }
}
