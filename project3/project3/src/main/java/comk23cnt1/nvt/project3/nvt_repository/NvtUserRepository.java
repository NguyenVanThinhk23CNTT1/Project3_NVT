package comk23cnt1.nvt.project3.nvt_repository;

import comk23cnt1.nvt.project3.nvt_entity.NvtUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NvtUserRepository extends JpaRepository<NvtUser, Long> {
    Optional<NvtUser> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<NvtUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
