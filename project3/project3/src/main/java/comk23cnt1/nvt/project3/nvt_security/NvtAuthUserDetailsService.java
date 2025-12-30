package comk23cnt1.nvt.project3.nvt_security;

import comk23cnt1.nvt.project3.nvt_entity.NvtUser;
import comk23cnt1.nvt.project3.nvt_repository.NvtUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NvtAuthUserDetailsService implements UserDetailsService {

    private final NvtUserRepository userRepo;

    public NvtAuthUserDetailsService(NvtUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NvtUser u = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user: " + username));

        boolean enabled = Boolean.TRUE.equals(u.getEnabled());
        boolean locked = u.getStatus() != null && u.getStatus().equalsIgnoreCase("LOCKED");

        return org.springframework.security.core.userdetails.User.builder()
                .username(u.getUsername())
                .password(u.getPassword()) // field password map -> password_hash
                .disabled(!enabled) // enabled=false => disabled
                .accountLocked(locked) // status=LOCKED => locked
                .authorities(List.of(new SimpleGrantedAuthority(u.getRole().getRoleWithPrefix()))) // ROLE_ADMIN/ROLE_USER
                .build();
    }
}
