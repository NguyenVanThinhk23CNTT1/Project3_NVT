package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtUser;
import comk23cnt1.nvt.project3.nvt_repository.NvtUserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller quản lý profile cho User
 * Chỉ USER và ADMIN mới có thể truy cập
 */
@Controller
@RequestMapping("/user/profile")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Method-level security
public class NvtUserProfileController {

    private final NvtUserRepository userRepository;

    public NvtUserProfileController(NvtUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Xem profile
     */
    @GetMapping
    public String viewProfile(Authentication auth, Model model) {
        String username = auth.getName();
        NvtUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        return "user/profile"; // templates/user/profile.html
    }

    /**
     * Form edit profile
     */
    @GetMapping("/edit")
    public String editProfileForm(Authentication auth, Model model) {
        String username = auth.getName();
        NvtUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        return "user/profile-edit"; // templates/user/profile-edit.html
    }

    /**
     * Xử lý update profile
     */
    @PostMapping("/edit")
    public String updateProfile(
            Authentication auth,
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String phone,
            RedirectAttributes redirectAttributes) {

        String username = auth.getName();
        NvtUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update thông tin
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);

        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Cập nhật profile thành công!");
        return "redirect:/user/profile";
    }

    /**
     * Form đổi password
     */
    @GetMapping("/change-password")
    public String changePasswordForm() {
        return "user/change-password"; // templates/user/change-password.html
    }

    /**
     * Xử lý đổi password
     * Note: Cần implement password validation và encoding
     */
    @PostMapping("/change-password")
    public String changePassword(
            Authentication auth,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {

        // TODO: Implement password change logic
        // 1. Verify current password
        // 2. Validate new password
        // 3. Encode and save new password

        redirectAttributes.addFlashAttribute("info", "Chức năng đổi mật khẩu đang được phát triển");
        return "redirect:/user/profile";
    }
}
