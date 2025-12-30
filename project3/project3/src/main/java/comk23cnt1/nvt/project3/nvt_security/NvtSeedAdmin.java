package comk23cnt1.nvt.project3.nvt_security;

import comk23cnt1.nvt.project3.nvt_entity.NvtUser;
import comk23cnt1.nvt.project3.nvt_enum.Role;
import comk23cnt1.nvt.project3.nvt_repository.NvtUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class NvtSeedAdmin implements CommandLineRunner {

    private final NvtUserRepository userRepo;
    private final PasswordEncoder encoder;

    public NvtSeedAdmin(NvtUserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        String username = "admin";

        if (!userRepo.existsByUsername(username)) {
            NvtUser u = new NvtUser();
            u.setUsername(username);
            u.setPassword(encoder.encode("123456")); // -> password_hash
            u.setRole(Role.ADMIN);
            u.setEnabled(true);
            u.setStatus("ACTIVE");
            u.setFullName("Administrator");
            u.setEmail("admin@nvt.local");

            userRepo.save(u);
            System.out.println(">>> Seed admin created: admin / 123456");
        }
    }
}
