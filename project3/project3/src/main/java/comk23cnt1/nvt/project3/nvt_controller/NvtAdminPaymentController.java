package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtBill;
import comk23cnt1.nvt.project3.nvt_entity.NvtPayment;
import comk23cnt1.nvt.project3.nvt_repository.NvtBillRepository;
import comk23cnt1.nvt.project3.nvt_repository.NvtPaymentRepository;
import comk23cnt1.nvt.project3.nvt_service.NvtMailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class NvtAdminPaymentController {

    private final NvtPaymentRepository paymentRepo;
    private final NvtBillRepository billRepo;
    private final NvtMailService mailService;

    public NvtAdminPaymentController(
            NvtPaymentRepository paymentRepo,
            NvtBillRepository billRepo,
            NvtMailService mailService) {
        this.paymentRepo = paymentRepo;
        this.billRepo = billRepo;
        this.mailService = mailService;
    }

    // ✅ CASH + BANK chung 1 trang
    @GetMapping("/cash-payments")
    public String pendingPayments(Model model,
                                  @RequestParam(required = false) String msg,
                                  @RequestParam(required = false) String err) {

        List<NvtPayment> pendingCash =
                paymentRepo.findByMethodAndStatusOrderByPaidAtDesc(
                        NvtPayment.Method.CASH,
                        NvtPayment.PayStatus.PENDING
                );

        List<NvtPayment> pendingBank =
                paymentRepo.findByMethodAndStatusOrderByPaidAtDesc(
                        NvtPayment.Method.BANK,
                        NvtPayment.PayStatus.PENDING
                );

        model.addAttribute("pendingCash", pendingCash);
        model.addAttribute("pendingBank", pendingBank);
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "admin/cash-payments";
    }

    // ✅ DUYỆT (CASH hoặc BANK đều vào đây)
    @PostMapping("/cash-payments/{paymentId}/confirm")
    public String confirm(@PathVariable Long paymentId) {
        try {
            NvtPayment p = paymentRepo.findById(paymentId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy payment"));

            if (p.getStatus() != NvtPayment.PayStatus.PENDING)
                throw new IllegalArgumentException("Payment không ở trạng thái PENDING");

            p.setStatus(NvtPayment.PayStatus.SUCCESS);
            p.setPaidAt(LocalDateTime.now());
            paymentRepo.save(p);

            NvtBill bill = billRepo.findById(p.getBillId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bill"));

            BigDecimal sumPaid = paymentRepo.findByBillIdOrderByPaidAtDesc(bill.getId())
                    .stream()
                    .filter(x -> x.getStatus() == NvtPayment.PayStatus.SUCCESS)
                    .map(NvtPayment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (sumPaid.compareTo(bill.getTotalAmount()) >= 0) {
                bill.setStatus(NvtBill.BillStatus.PAID);
                billRepo.save(bill);
            }

            // ✅ GỬI MAIL SAU KHI ADMIN DUYỆT
            if (p.getPayerEmail() != null) {
                mailService.sendPaymentSuccessEmail(
                        p.getPayerEmail(),
                        "Xác nhận thanh toán hóa đơn",
                        bill,
                        p.getAmount()
                );
            }

            return "redirect:/admin/cash-payments?msg=" + enc("Đã xác nhận & gửi email");
        } catch (Exception e) {
            return "redirect:/admin/cash-payments?err=" + enc(e.getMessage());
        }
    }

    @PostMapping("/cash-payments/{paymentId}/reject")
    public String reject(@PathVariable Long paymentId) {
        try {
            NvtPayment p = paymentRepo.findById(paymentId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy payment"));

            p.setStatus(NvtPayment.PayStatus.FAILED);
            paymentRepo.save(p);

            return "redirect:/admin/cash-payments?msg=" + enc("Đã từ chối yêu cầu");
        } catch (Exception e) {
            return "redirect:/admin/cash-payments?err=" + enc(e.getMessage());
        }
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}
