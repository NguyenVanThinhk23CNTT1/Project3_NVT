package comk23cnt1.nvt.project3.nvt_repository;

import comk23cnt1.nvt.project3.nvt_entity.NvtTenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NvtTenantRepository extends JpaRepository<NvtTenant, Long> {
    boolean existsByPhone(String phone);
    boolean existsByPhoneAndIdNot(String phone, Long id);

    boolean existsByCccd(String cccd);
    boolean existsByCccdAndIdNot(String cccd, Long id);
}
