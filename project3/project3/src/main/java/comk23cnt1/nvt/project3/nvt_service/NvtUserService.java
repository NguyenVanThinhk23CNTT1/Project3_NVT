package comk23cnt1.nvt.project3.nvt_service;

import comk23cnt1.nvt.project3.nvt_entity.NvtUser;
import comk23cnt1.nvt.project3.nvt_enum.Role;
import comk23cnt1.nvt.project3.nvt_repository.NvtUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service xử lý business logic cho User management
 */
@Service
public class NvtUserService {

    private final NvtUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public NvtUserService(NvtUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Tạo user mới
     */
    @Transactional
    public NvtUser createUser(String username, String password, String fullName,
            String email, String phone, Role role) {
        // Validate username không trùng
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username đã tồn tại: " + username);
        }

        // Validate email không trùng
        if (email != null && userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã tồn tại: " + email);
        }

        NvtUser user = new NvtUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(role != null ? role : Role.USER); // Default là USER
        user.setEnabled(true);
        user.setStatus("ACTIVE");

        return userRepository.save(user);
    }

    /**
     * Cập nhật thông tin user
     */
    @Transactional
    public NvtUser updateUser(Long userId, String fullName, String email, String phone) {
        NvtUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);

        return userRepository.save(user);
    }

    /**
     * Đổi mật khẩu
     */
    @Transactional
    public void changePassword(String username, String currentPassword, String newPassword) {
        NvtUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không đúng");
        }

        // Validate new password
        if (newPassword == null || newPassword.length() < 6) {
            throw new RuntimeException("Mật khẩu mới phải có ít nhất 6 ký tự");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Tìm user theo username
     */
    public Optional<NvtUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Tìm user theo email
     */
    public Optional<NvtUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Lock/Unlock user
     */
    @Transactional
    public void toggleUserStatus(Long userId) {
        NvtUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        if ("ACTIVE".equals(user.getStatus())) {
            user.setStatus("LOCKED");
        } else {
            user.setStatus("ACTIVE");
        }

        userRepository.save(user);
    }

    /**
     * Enable/Disable user
     */
    @Transactional
    public void toggleUserEnabled(Long userId) {
        NvtUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        user.setEnabled(!user.getEnabled());
        userRepository.save(user);
    }

    /**
     * Encode password (for admin reset password)
     */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
