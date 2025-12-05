package comk23cnt1.nvt.project3.nvt_repository;

import comk23cnt1.nvt.project3.nvt_entity.NvtPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NvtPaymentRepository extends JpaRepository<NvtPayment, Long> {
    List<NvtPayment> findByBillIdOrderByPaidAtDesc(Long billId);
}
