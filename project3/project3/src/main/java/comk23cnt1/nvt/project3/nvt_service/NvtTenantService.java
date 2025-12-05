package comk23cnt1.nvt.project3.nvt_service;

import comk23cnt1.nvt.project3.nvt_entity.NvtTenant;

import java.util.List;

public interface NvtTenantService {
    List<NvtTenant> findAll();
    NvtTenant findById(Long id);
    NvtTenant create(NvtTenant tenant);
    NvtTenant update(Long id, NvtTenant tenant);
    void delete(Long id);
}
