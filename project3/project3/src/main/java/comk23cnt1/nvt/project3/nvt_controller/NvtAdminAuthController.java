package comk23cnt1.nvt.project3.nvt_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NvtAdminAuthController {

    @GetMapping("/admin/login")
    public String loginPage(Boolean error, Boolean logout, Model model) {
        // Thymeleaf check query param: ?error=true / ?logout=true
        return "admin/login";
    }
}
