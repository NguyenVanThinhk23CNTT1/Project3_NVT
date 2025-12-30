package comk23cnt1.nvt.project3.nvt_service;

import comk23cnt1.nvt.project3.nvt_entity.NvtBill;
import comk23cnt1.nvt.project3.nvt_entity.NvtPayment;
import comk23cnt1.nvt.project3.nvt_repository.NvtBillRepository;
import comk23cnt1.nvt.project3.nvt_repository.NvtPaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class NvtRevenueService {

    private final NvtBillRepository billRepository;
    private final NvtPaymentRepository paymentRepository;

    public NvtRevenueService(NvtBillRepository billRepository, NvtPaymentRepository paymentRepository) {
        this.billRepository = billRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Lấy tổng doanh thu theo năm (bills đã PAID)
     */
    public BigDecimal getTotalRevenueByYear(int year) {
        BigDecimal result = billRepository.sumTotalPaidByYear(year);
        return result != null ? result : BigDecimal.ZERO;
    }

    /**
     * Lấy doanh thu theo từng tháng trong năm (cho biểu đồ Line)
     */
    public List<BigDecimal> getMonthlyRevenueData(int year) {
        List<BigDecimal> monthlyData = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            BigDecimal amount = billRepository.sumTotalPaidByMonthAndYear(month, year);
            monthlyData.add(amount != null ? amount : BigDecimal.ZERO);
        }
        return monthlyData;
    }

    /**
     * Lấy doanh thu theo loại (tiền phòng, điện, nước, dịch vụ)
     */
    public Map<String, BigDecimal> getRevenueByCategory(int year) {
        Map<String, BigDecimal> categories = new LinkedHashMap<>();

        BigDecimal rent = billRepository.sumRentPaidByYear(year);
        BigDecimal electric = billRepository.sumElectricPaidByYear(year);
        BigDecimal water = billRepository.sumWaterPaidByYear(year);
        BigDecimal service = billRepository.sumServicePaidByYear(year);

        categories.put("Tiền phòng", rent != null ? rent : BigDecimal.ZERO);
        categories.put("Tiền điện", electric != null ? electric : BigDecimal.ZERO);
        categories.put("Tiền nước", water != null ? water : BigDecimal.ZERO);
        categories.put("Dịch vụ", service != null ? service : BigDecimal.ZERO);

        return categories;
    }

    /**
     * Thống kê theo phương thức thanh toán
     */
    public Map<String, Object> getPaymentMethodStats(int year) {
        Map<String, Object> stats = new LinkedHashMap<>();
        List<Object[]> results = paymentRepository.countByMethodAndYear(year);

        Map<String, Long> countByMethod = new LinkedHashMap<>();
        Map<String, BigDecimal> amountByMethod = new LinkedHashMap<>();

        // Khởi tạo tất cả methods với giá trị 0
        for (NvtPayment.Method method : NvtPayment.Method.values()) {
            countByMethod.put(method.name(), 0L);
            amountByMethod.put(method.name(), BigDecimal.ZERO);
        }

        // Điền dữ liệu thực
        for (Object[] row : results) {
            NvtPayment.Method method = (NvtPayment.Method) row[0];
            Long count = (Long) row[1];
            BigDecimal amount = (BigDecimal) row[2];
            countByMethod.put(method.name(), count);
            amountByMethod.put(method.name(), amount != null ? amount : BigDecimal.ZERO);
        }

        stats.put("count", countByMethod);
        stats.put("amount", amountByMethod);
        return stats;
    }

    /**
     * Tính tỷ lệ thu hồi nợ (% bills đã thanh toán / tổng bills)
     */
    public BigDecimal getCollectionRate(int year) {
        BigDecimal totalAll = billRepository.sumTotalAllByYear(year);
        BigDecimal totalPaid = billRepository.sumTotalPaidByYear(year);

        if (totalAll == null || totalAll.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        if (totalPaid == null) {
            totalPaid = BigDecimal.ZERO;
        }

        return totalPaid.multiply(BigDecimal.valueOf(100))
                .divide(totalAll, 2, RoundingMode.HALF_UP);
    }

    /**
     * Đếm số bills theo trạng thái
     */
    public Map<String, Long> getBillStatusCount() {
        Map<String, Long> statusCount = new LinkedHashMap<>();
        statusCount.put("PAID", billRepository.countByStatus(NvtBill.BillStatus.PAID));
        statusCount.put("UNPAID", billRepository.countByStatus(NvtBill.BillStatus.UNPAID));
        statusCount.put("OVERDUE", billRepository.countByStatus(NvtBill.BillStatus.OVERDUE));
        return statusCount;
    }

    /**
     * Lấy danh sách năm có dữ liệu (để chọn filter)
     */
    public List<Integer> getAvailableYears() {
        List<Integer> years = new ArrayList<>();
        int currentYear = java.time.Year.now().getValue();
        // Lấy 5 năm gần nhất
        for (int y = currentYear; y >= currentYear - 4; y--) {
            years.add(y);
        }
        return years;
    }
}
