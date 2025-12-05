package comk23cnt1.nvt.project3.nvt_service.impl;

import comk23cnt1.nvt.project3.nvt_entity.NvtTenant;
import comk23cnt1.nvt.project3.nvt_repository.NvtTenantRepository;
import comk23cnt1.nvt.project3.nvt_service.NvtTenantService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NvtTenantServiceImpl implements NvtTenantService {

    private final NvtTenantRepository tenantRepository;

    public NvtTenantServiceImpl(NvtTenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public List<NvtTenant> findAll() {
        return tenantRepository.findAll();
    }

    @Override
    public NvtTenant findById(Long id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách thuê ID=" + id));
    }

    @Override
    public NvtTenant create(NvtTenant t) {
        normalize(t);

        if (t.getFullName() == null || t.getFullName().isBlank())
            throw new IllegalArgumentException("Họ tên không được rỗng");

        if (t.getPhone() == null || t.getPhone().isBlank())
            throw new IllegalArgumentException("Số điện thoại không được rỗng");

        if (tenantRepository.existsByPhone(t.getPhone()))
            throw new IllegalArgumentException("Số điện thoại đã tồn tại");

        if (t.getCccd() != null && tenantRepository.existsByCccd(t.getCccd()))
            throw new IllegalArgumentException("CCCD đã tồn tại");

        if (t.getStatus() == null) t.setStatus(NvtTenant.TenantStatus.ACTIVE);

        return tenantRepository.save(t);
    }

    @Override
    public NvtTenant update(Long id, NvtTenant t) {
        NvtTenant existing = findById(id);
        normalize(t);

        if (t.getFullName() == null || t.getFullName().isBlank())
            throw new IllegalArgumentException("Họ tên không được rỗng");

        if (t.getPhone() == null || t.getPhone().isBlank())
            throw new IllegalArgumentException("Số điện thoại không được rỗng");

        if (tenantRepository.existsByPhoneAndIdNot(t.getPhone(), id))
            throw new IllegalArgumentException("Số điện thoại đã tồn tại (trùng người khác)");

        if (t.getCccd() != null && tenantRepository.existsByCccdAndIdNot(t.getCccd(), id))
            throw new IllegalArgumentException("CCCD đã tồn tại (trùng người khác)");

        existing.setFullName(t.getFullName());
        existing.setPhone(t.getPhone());
        existing.setCccd(t.getCccd());
        existing.setBirthday(t.getBirthday());
        existing.setAddress(t.getAddress());
        existing.setStatus(t.getStatus() == null ? NvtTenant.TenantStatus.ACTIVE : t.getStatus());

        return tenantRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!tenantRepository.existsById(id))
            throw new IllegalArgumentException("Không tìm thấy khách để xóa: ID=" + id);

        // Lưu ý: Sau này khi tenant đã nằm trong hợp đồng, bạn sẽ chặn xóa ở đây.
        tenantRepository.deleteById(id);
    }

    private void normalize(NvtTenant t) {
        if (t.getFullName() != null) t.setFullName(t.getFullName().trim());
        if (t.getPhone() != null) t.setPhone(t.getPhone().trim());
        if (t.getCccd() != null) {
            String c = t.getCccd().trim();
            t.setCccd(c.isBlank() ? null : c);
        }
        if (t.getAddress() != null) {
            String a = t.getAddress().trim();
            t.setAddress(a.isBlank() ? null : a);
        }
    }
}
