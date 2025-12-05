package comk23cnt1.nvt.project3.nvt_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NvtAdminPageController {

    @GetMapping("/admin")
    public String adminHome() {
        return "admin/index";
    }
}
