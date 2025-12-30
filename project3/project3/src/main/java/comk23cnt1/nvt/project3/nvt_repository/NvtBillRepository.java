package comk23cnt1.nvt.project3.nvt_repository;

import comk23cnt1.nvt.project3.nvt_entity.NvtBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface NvtBillRepository extends JpaRepository<NvtBill, Long> {

    boolean existsByContractIdAndBillMonthAndBillYear(Long contractId, Integer billMonth, Integer billYear);

    List<NvtBill> findByContractIdOrderByBillYearDescBillMonthDesc(Long contractId);

    List<NvtBill> findByRoomIdOrderByBillYearDescBillMonthDesc(Long roomId);

    // ✅ thêm để dashboard đếm nhanh
    long countByStatus(NvtBill.BillStatus status);

    // ========== REVENUE STATISTICS ==========

    // Tổng doanh thu theo năm (bills đã PAID)
    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM NvtBill b WHERE b.status = 'PAID' AND b.billYear = :year")
    BigDecimal sumTotalPaidByYear(@Param("year") Integer year);

    // Tổng doanh thu theo tháng/năm
    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM NvtBill b WHERE b.status = 'PAID' AND b.billMonth = :month AND b.billYear = :year")
    BigDecimal sumTotalPaidByMonthAndYear(@Param("month") Integer month, @Param("year") Integer year);

    // Tổng tiền phòng theo năm
    @Query("SELECT COALESCE(SUM(b.rentAmount), 0) FROM NvtBill b WHERE b.status = 'PAID' AND b.billYear = :year")
    BigDecimal sumRentPaidByYear(@Param("year") Integer year);

    // Tổng tiền điện theo năm
    @Query("SELECT COALESCE(SUM(b.electricAmount), 0) FROM NvtBill b WHERE b.status = 'PAID' AND b.billYear = :year")
    BigDecimal sumElectricPaidByYear(@Param("year") Integer year);

    // Tổng tiền nước theo năm
    @Query("SELECT COALESCE(SUM(b.waterAmount), 0) FROM NvtBill b WHERE b.status = 'PAID' AND b.billYear = :year")
    BigDecimal sumWaterPaidByYear(@Param("year") Integer year);

    // Tổng tiền dịch vụ theo năm
    @Query("SELECT COALESCE(SUM(b.serviceAmount), 0) FROM NvtBill b WHERE b.status = 'PAID' AND b.billYear = :year")
    BigDecimal sumServicePaidByYear(@Param("year") Integer year);

    // Tổng tất cả bills theo năm (cả PAID, UNPAID, OVERDUE)
    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM NvtBill b WHERE b.billYear = :year")
    BigDecimal sumTotalAllByYear(@Param("year") Integer year);

    // Lấy danh sách bills theo năm
    List<NvtBill> findByBillYearOrderByBillMonthAsc(Integer billYear);

    // Lấy danh sách bills đã thanh toán theo năm
    List<NvtBill> findByStatusAndBillYearOrderByBillMonthAsc(NvtBill.BillStatus status, Integer billYear);
}
