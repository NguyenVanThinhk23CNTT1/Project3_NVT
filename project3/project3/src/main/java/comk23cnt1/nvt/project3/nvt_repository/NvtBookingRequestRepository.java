package comk23cnt1.nvt.project3.nvt_repository;

import comk23cnt1.nvt.project3.nvt_entity.NvtBookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NvtBookingRequestRepository extends JpaRepository<NvtBookingRequest, Long> {
    List<NvtBookingRequest> findByStatusOrderByCreatedAtDesc(NvtBookingRequest.Status status);
    List<NvtBookingRequest> findByRoomIdOrderByCreatedAtDesc(Long roomId);
    long countByStatus(NvtBookingRequest.Status status);
}
