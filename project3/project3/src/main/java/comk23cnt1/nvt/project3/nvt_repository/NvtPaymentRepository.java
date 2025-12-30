package comk23cnt1.nvt.project3.nvt_repository;

import comk23cnt1.nvt.project3.nvt_entity.NvtPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface NvtPaymentRepository extends JpaRepository<NvtPayment, Long> {
    List<NvtPayment> findByBillIdOrderByPaidAtDesc(Long billId);

    // ✅ thêm
    List<NvtPayment> findByMethodAndStatusOrderByPaidAtDesc(NvtPayment.Method method, NvtPayment.PayStatus status);

    // ========== PAYMENT STATISTICS ==========

    // Thống kê theo phương thức thanh toán trong năm
    @Query("SELECT p.method, COUNT(p), COALESCE(SUM(p.amount), 0) FROM NvtPayment p WHERE p.status = 'SUCCESS' AND YEAR(p.paidAt) = :year GROUP BY p.method")
    List<Object[]> countByMethodAndYear(@Param("year") Integer year);

    // Tổng thanh toán thành công theo năm
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM NvtPayment p WHERE p.status = 'SUCCESS' AND YEAR(p.paidAt) = :year")
    BigDecimal sumSuccessPaymentsByYear(@Param("year") Integer year);

    // Đếm số lượng thanh toán thành công theo năm
    @Query("SELECT COUNT(p) FROM NvtPayment p WHERE p.status = 'SUCCESS' AND YEAR(p.paidAt) = :year")
    Long countSuccessPaymentsByYear(@Param("year") Integer year);
}
