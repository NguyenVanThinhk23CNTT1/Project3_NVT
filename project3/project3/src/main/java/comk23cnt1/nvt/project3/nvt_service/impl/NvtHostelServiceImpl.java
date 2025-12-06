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
            throw new IllegalArgumentException("Địa chỉ không được rỗng");

        // Bạn đang có existsByName trong repo -> dùng luôn
        if (hostelRepository.existsByName(h.getName()))
            throw new IllegalArgumentException("Tên nhà trọ đã tồn tại");

        return hostelRepository.save(h);
    }

    @Override
    public NvtHostel update(Long id, NvtHostel h) {
        NvtHostel existing = findById(id);
        normalize(h);

        if (h.getName() == null || h.getName().isBlank())
            throw new IllegalArgumentException("Tên nhà trọ không được rỗng");

        if (h.getAddress() == null || h.getAddress().isBlank())
            throw new IllegalArgumentException("Địa chỉ không được rỗng");

        // nếu đổi tên -> tránh trùng
        if (!existing.getName().equalsIgnoreCase(h.getName())
                && hostelRepository.existsByName(h.getName())) {
            throw new IllegalArgumentException("Tên nhà trọ đã tồn tại");
        }

        existing.setName(h.getName());
        existing.setAddress(h.getAddress());
        existing.setPhone(h.getPhone());

        return hostelRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!hostelRepository.existsById(id))
            throw new IllegalArgumentException("Không tìm thấy nhà trọ để xóa: ID=" + id);

        // Nếu sau này muốn chặn xóa khi đang có rooms thuộc hostel -> chặn ở đây
        hostelRepository.deleteById(id);
    }

    private void normalize(NvtHostel h) {
        if (h.getName() != null) h.setName(h.getName().trim());
        if (h.getAddress() != null) h.setAddress(h.getAddress().trim());
        if (h.getPhone() != null) {
            String p = h.getPhone().trim();
            h.setPhone(p.isBlank() ? null : p);
        }
    }
}
