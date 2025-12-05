package comk23cnt1.nvt.project3.nvt_repository;

import comk23cnt1.nvt.project3.nvt_entity.NvtContractMember;
import comk23cnt1.nvt.project3.nvt_entity.NvtContractMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NvtContractMemberRepository extends JpaRepository<NvtContractMember, NvtContractMemberId> {

    List<NvtContractMember> findByContractIdOrderByTenantIdAsc(Long contractId);

    Optional<NvtContractMember> findByContractIdAndTenantId(Long contractId, Long tenantId);

    boolean existsByContractIdAndTenantId(Long contractId, Long tenantId);

    long countByContractId(Long contractId);

    void deleteByContractIdAndTenantId(Long contractId, Long tenantId);

    void deleteByContractId(Long contractId);
}
