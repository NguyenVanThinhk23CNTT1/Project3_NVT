package comk23cnt1.nvt.project3.nvt_service;

import comk23cnt1.nvt.project3.nvt_entity.NvtContract;
import comk23cnt1.nvt.project3.nvt_entity.NvtContractMember;

import java.util.List;

public interface NvtContractService {

    List<NvtContract> findAll();
    NvtContract findById(Long id);

    NvtContract create(NvtContract c);

    List<NvtContractMember> members(Long contractId);

    void addMember(Long contractId, NvtContractMember m);
    void removeMember(Long contractId, Long tenantId);

    void endContract(Long contractId);
}
