package comk23cnt1.nvt.project3.nvt_repository;

import comk23cnt1.nvt.project3.nvt_entity.NvtContract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NvtContractRepository extends JpaRepository<NvtContract, Long> {

    boolean existsByContractCode(String contractCode);

    Optional<NvtContract> findByContractCode(String contractCode);

    List<NvtContract> findAllByOrderByIdDesc();

    List<NvtContract> findByRoomIdOrderByIdDesc(Long roomId);

    List<NvtContract> findByTenantIdOrderByIdDesc(Long tenantId);
}
