package comk23cnt1.nvt.project3.nvt_repository;

import comk23cnt1.nvt.project3.nvt_entity.NvtHostel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NvtHostelRepository extends JpaRepository<NvtHostel, Long> {
    boolean existsByName(String name);
}
