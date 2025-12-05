package comk23cnt1.nvt.project3.nvt_service.impl;

import comk23cnt1.nvt.project3.nvt_entity.NvtHostel;
import comk23cnt1.nvt.project3.nvt_repository.NvtHostelRepository;
import comk23cnt1.nvt.project3.nvt_service.NvtHostelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NvtHostelServiceImpl implements NvtHostelService {

    private final NvtHostelRepository hostelRepository;

    public NvtHostelServiceImpl(NvtHostelRepository hostelRepository) {
        this.hostelRepository = hostelRepository;
    }

    @Override
    public List<NvtHostel> findAll() {
        return hostelRepository.findAll();
    }

    @Override
    public NvtHostel findById(Long id) {
        return hostelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhà trọ ID=" + id));
    }

    @Override
    public NvtHostel create(NvtHostel h) {
        normalize(h);

        if (h.getName() == null || h.getName().isBlank())
            throw new IllegalArgumentException("Tên nhà trọ không được rỗng");

        if (h.getAddress() == null || h.getAddress().isBlank())
            throw new IllegalArgumentException("Địa chỉ nhà trọ không được rỗng");

        if (hostelRepository.existsByName(h.getName()))
            throw new IllegalArgumentException("Tên nhà trọ đã tồn tại");

        return hostelRepository.save(h);
    }

    @Override
    public void delete(Long id) {
        if (!hostelRepository.existsById(id))
            throw new IllegalArgumentException("Không tìm thấy nhà trọ để xóa: ID=" + id);

        hostelRepository.deleteById(id);
    }

    private void normalize(NvtHostel h) {
        if (h == null) return;
        if (h.getName() != null) h.setName(h.getName().trim());
        if (h.getAddress() != null) h.setAddress(h.getAddress().trim());
        if (h.getPhone() != null) h.setPhone(h.getPhone().trim());
    }
}
