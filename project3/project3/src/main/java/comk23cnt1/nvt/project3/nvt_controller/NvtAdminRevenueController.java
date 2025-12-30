package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_service.NvtRevenueService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class NvtAdminRevenueController {

    private final NvtRevenueService revenueService;

    public NvtAdminRevenueController(NvtRevenueService revenueService) {
        this.revenueService = revenueService;
    }

    /**
     * Trang thống kê doanh thu
     */
    @GetMapping("/admin/revenue")
    public String revenuePage(
            @RequestParam(value = "year", required = false) Integer year,
            Model model) {

        // Mặc định là năm hiện tại
        if (year == null) {
            year = Year.now().getValue();
        }

        // Tổng doanh thu năm
        BigDecimal totalRevenue = revenueService.getTotalRevenueByYear(year);

        // Doanh thu theo tháng (12 tháng)
        List<BigDecimal> monthlyData = revenueService.getMonthlyRevenueData(year);

        // Doanh thu theo loại
        Map<String, BigDecimal> categoryData = revenueService.getRevenueByCategory(year);

        // Thống kê phương thức thanh toán
        Map<String, Object> paymentStats = revenueService.getPaymentMethodStats(year);

        // Tỷ lệ thu hồi nợ
        BigDecimal collectionRate = revenueService.getCollectionRate(year);

        // Đếm bills theo trạng thái
        Map<String, Long> billStatusCount = revenueService.getBillStatusCount();

        // Danh sách năm để filter
        List<Integer> availableYears = revenueService.getAvailableYears();

        model.addAttribute("selectedYear", year);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("monthlyData", monthlyData);
        model.addAttribute("categoryData", categoryData);
        model.addAttribute("paymentStats", paymentStats);
        model.addAttribute("collectionRate", collectionRate);
        model.addAttribute("billStatusCount", billStatusCount);
        model.addAttribute("availableYears", availableYears);

        return "admin/revenue";
    }

    /**
     * API trả về dữ liệu JSON cho charts (dùng AJAX nếu cần)
     */
    @GetMapping("/admin/revenue/api/data")
    @ResponseBody
    public Map<String, Object> getRevenueData(@RequestParam(value = "year", required = false) Integer year) {
        if (year == null) {
            year = Year.now().getValue();
        }

        Map<String, Object> data = new HashMap<>();
        data.put("year", year);
        data.put("totalRevenue", revenueService.getTotalRevenueByYear(year));
        data.put("monthlyData", revenueService.getMonthlyRevenueData(year));
        data.put("categoryData", revenueService.getRevenueByCategory(year));
        data.put("paymentStats", revenueService.getPaymentMethodStats(year));
        data.put("collectionRate", revenueService.getCollectionRate(year));
        data.put("billStatusCount", revenueService.getBillStatusCount());

        return data;
    }
}
