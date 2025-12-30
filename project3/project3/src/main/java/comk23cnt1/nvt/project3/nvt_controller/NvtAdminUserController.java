package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtUser;
import comk23cnt1.nvt.project3.nvt_enum.Role;
import comk23cnt1.nvt.project3.nvt_repository.NvtUserRepository;
import comk23cnt1.nvt.project3.nvt_service.NvtUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller quản lý User (CRUD) cho Admin
 */
@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class NvtAdminUserController {

    private final NvtUserRepository userRepository;
    private final NvtUserService userService;

    public NvtAdminUserController(NvtUserRepository userRepository, NvtUserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Danh sách tất cả users
     */
    @GetMapping
    public String listUsers(Model model) {
        List<NvtUser> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/users/list";
    }

    /**
     * Form tạo user mới
     */
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("user", new NvtUser());
        model.addAttribute("roles", Role.values());
        return "admin/users/form";
    }

    /**
     * Xử lý tạo user mới
     */
    @PostMapping("/create")
    public String create(@RequestParam String username,
            @RequestParam String password,
            @RequestParam String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam Role role,
            RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(username, password, fullName, email, phone, role);
            redirectAttributes.addFlashAttribute("success", "Tạo user thành công!");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/users/create";
        }
    }

    /**
     * Form edit user
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        NvtUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "admin/users/form";
    }

    /**
     * Xử lý update user
     */
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
            @RequestParam String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam Role role,
            @RequestParam Boolean enabled,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {
        try {
            NvtUser user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));

            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhone(phone);
            user.setRole(role);
            user.setEnabled(enabled);
            user.setStatus(status);

            userRepository.save(user);

            redirectAttributes.addFlashAttribute("success", "Cập nhật user thành công!");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/users/" + id + "/edit";
        }
    }

    /**
     * Xóa user
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            NvtUser user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));

            // Không cho xóa chính mình
            // TODO: Implement check current user

            userRepository.delete(user);
            redirectAttributes.addFlashAttribute("success", "Xóa user thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    /**
     * Reset password
     */
    @PostMapping("/{id}/reset-password")
    public String resetPassword(@PathVariable Long id,
            @RequestParam String newPassword,
            RedirectAttributes redirectAttributes) {
        try {
            NvtUser user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));

            // Reset password using service
            user.setPassword(userService.encodePassword(newPassword));
            userRepository.save(user);

            redirectAttributes.addFlashAttribute("success", "Reset password thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    /**
     * Toggle enabled status
     */
    @PostMapping("/{id}/toggle-enabled")
    public String toggleEnabled(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.toggleUserEnabled(id);
            redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    /**
     * Toggle lock status
     */
    @PostMapping("/{id}/toggle-lock")
    public String toggleLock(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.toggleUserStatus(id);
            redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái khóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
