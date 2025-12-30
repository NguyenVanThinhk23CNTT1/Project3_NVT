package comk23cnt1.nvt.project3.nvt_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller xử lý trang login chung cho cả Admin và User
 */
@Controller
public class NvtLoginController {

    /**
     * Trang login chung
     * Sau khi login thành công, tất cả user đều redirect về /user/home
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html
    }
}
