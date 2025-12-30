package comk23cnt1.nvt.project3.nvt_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller xử lý trang access denied
 */
@Controller
public class NvtAccessDeniedController {

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied"; // templates/error/access-denied.html
    }
}
