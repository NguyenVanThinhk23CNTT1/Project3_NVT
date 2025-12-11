package comk23cnt1.nvt.project3.nvt_service.impl;

import comk23cnt1.nvt.project3.nvt_entity.NvtBookingRequest;
import comk23cnt1.nvt.project3.nvt_repository.NvtBookingRequestRepository;
import comk23cnt1.nvt.project3.nvt_service.NvtBookingRequestService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class NvtBookingRequestServiceImpl implements NvtBookingRequestService {

    private final NvtBookingRequestRepository repo;

    public NvtBookingRequestServiceImpl(NvtBookingRequestRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<NvtBookingRequest> findAll() {
        // sort để khỏi cần thêm method mới
        return repo.findAll().stream()
                .sorted(Comparator.comparing(NvtBookingRequest::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .toList();
    }

    @Override
    public List<NvtBookingRequest> findByStatus(NvtBookingRequest.Status status) {
        if (status == null) return findAll();
        return repo.findByStatusOrderByCreatedAtDesc(status);
    }

    @Override
    public long countByStatus(NvtBookingRequest.Status status) {
        return repo.countByStatus(status);
    }

    @Override
    public NvtBookingRequest create(NvtBookingRequest req) {
        if (req.getRoomId() == null) throw new IllegalArgumentException("Thiếu roomId");
        if (req.getFullName() == null || req.getFullName().trim().isBlank())
            throw new IllegalArgumentException("Họ tên không được rỗng");
        if (req.getPhone() == null || req.getPhone().trim().isBlank())
            throw new IllegalArgumentException("SĐT không được rỗng");

        req.setFullName(req.getFullName().trim());
        req.setPhone(req.getPhone().trim());
        if (req.getNote() != null) req.setNote(req.getNote().trim());
        if (req.getStatus() == null) req.setStatus(NvtBookingRequest.Status.NEW);

        return repo.save(req);
    }

    @Override
    public NvtBookingRequest updateStatus(Long id, NvtBookingRequest.Status status, String adminNote) {
        NvtBookingRequest req = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy booking request ID=" + id));

        req.setStatus(status == null ? NvtBookingRequest.Status.NEW : status);
        req.setAdminNote(adminNote == null ? null : adminNote.trim());

        return repo.save(req);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new IllegalArgumentException("Không tìm thấy booking request để xóa: ID=" + id);
        repo.deleteById(id);
    }
}
