package comk23cnt1.nvt.project3.nvt_controller;

import org.springframework.stereotype.Controller;

/**
 * ❗ FILE NÀY ĐÃ BỊ VÔ HIỆU HÓA
 * Vì project đã dùng NvtAdminPaymentController để xử lý duyệt CASH + BANK chung 1 trang.
 * Nếu giữ controller này sẽ bị Ambiguous mapping với /admin/cash-payments/**.
 *
 * => Bạn có thể XÓA hẳn file này cũng được.
 */
@Controller
public class NvtAdminCashPaymentController {
    // intentionally empty
}
