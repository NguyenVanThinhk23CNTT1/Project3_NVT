package comk23cnt1.nvt.project3.nvt_service;

import comk23cnt1.nvt.project3.nvt_entity.NvtBill;
import comk23cnt1.nvt.project3.nvt_entity.NvtPayment;

import java.util.List;

public interface NvtBillingService {
    List<NvtBill> findAllBills();
    NvtBill findBill(Long billId);

    NvtBill createBill(Long contractId, Integer month, Integer year);
    List<NvtPayment> payments(Long billId);
    NvtPayment addPayment(Long billId, NvtPayment payment);
}
