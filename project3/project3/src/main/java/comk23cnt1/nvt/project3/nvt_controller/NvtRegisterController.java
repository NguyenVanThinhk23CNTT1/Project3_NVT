package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_enum.Role;
import comk23cnt1.nvt.project3.nvt_service.NvtUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller xử lý đăng ký user mới
 */
@Controller
public class NvtRegisterController {

    private final NvtUserService userService;

    public NvtRegisterController(NvtUserService userService) {
        this.userService = userService;
    }

    /**
     * Hiển thị form đăng ký
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        return "register";
    }

    /**
     * Xử lý đăng ký user mới
     */
    @PostMapping("/register")
    public String register(@RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            RedirectAttributes redirectAttributes) {
        try {
            // Validate password match
            if (!password.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
                redirectAttributes.addFlashAttribute("username", username);
                redirectAttributes.addFlashAttribute("fullName", fullName);
                redirectAttributes.addFlashAttribute("email", email);
                redirectAttributes.addFlashAttribute("phone", phone);
                return "redirect:/register";
            }

            // Validate password length
            if (password.length() < 6) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
                redirectAttributes.addFlashAttribute("username", username);
                redirectAttributes.addFlashAttribute("fullName", fullName);
                redirectAttributes.addFlashAttribute("email", email);
                redirectAttributes.addFlashAttribute("phone", phone);
                return "redirect:/register";
            }

            // Validate username length
            if (username.length() < 3) {
                redirectAttributes.addFlashAttribute("error", "Username phải có ít nhất 3 ký tự!");
                redirectAttributes.addFlashAttribute("username", username);
                redirectAttributes.addFlashAttribute("fullName", fullName);
                redirectAttributes.addFlashAttribute("email", email);
                redirectAttributes.addFlashAttribute("phone", phone);
                return "redirect:/register";
            }

            // Create user with USER role
            userService.createUser(username, password, fullName, email, phone, Role.USER);

            redirectAttributes.addFlashAttribute("success",
                    "Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            redirectAttributes.addFlashAttribute("username", username);
            redirectAttributes.addFlashAttribute("fullName", fullName);
            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("phone", phone);
            return "redirect:/register";
        }
    }
}
