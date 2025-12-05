package comk23cnt1.nvt.project3.nvt_service;

import comk23cnt1.nvt.project3.nvt_entity.NvtHostel;

import java.util.List;

public interface NvtHostelService {
    List<NvtHostel> findAll();
    NvtHostel findById(Long id);
    NvtHostel create(NvtHostel hostel);
    void delete(Long id);
}
