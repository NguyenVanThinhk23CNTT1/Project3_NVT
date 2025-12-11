package comk23cnt1.nvt.project3.nvt_service;

import comk23cnt1.nvt.project3.nvt_entity.NvtBookingRequest;

import java.util.List;

public interface NvtBookingRequestService {
    List<NvtBookingRequest> findAll();
    List<NvtBookingRequest> findByStatus(NvtBookingRequest.Status status); // status null => all
    long countByStatus(NvtBookingRequest.Status status);

    NvtBookingRequest create(NvtBookingRequest req);
    NvtBookingRequest updateStatus(Long id, NvtBookingRequest.Status status, String adminNote);
    void delete(Long id);
}
