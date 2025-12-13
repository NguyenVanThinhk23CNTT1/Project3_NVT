package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtBill;
import comk23cnt1.nvt.project3.nvt_entity.NvtPayment;
import comk23cnt1.nvt.project3.nvt_repository.NvtBillRepository;
import comk23cnt1.nvt.project3.nvt_repository.NvtPaymentRepository;
import comk23cnt1.nvt.project3.nvt_service.NvtMailService;
import comk23cnt1.nvt.project3.nvt_service.NvtBillingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/admin/cash-payments")
public class NvtAdminCashPaymentController {

    private final NvtPaymentRepository paymentRepo;
    private final NvtBillRepository billRepo;
    private final NvtBillingService billingService;
    private final NvtMailService mailService;

    public NvtAdminCashPaymentController(NvtPaymentRepository paymentRepo,
                                         NvtBillRepository billRepo,
                                         NvtBillingService billingService,
                                         NvtMailService mailService) {
        this.paymentRepo = paymentRepo;
        this.billRepo = billRepo;
        this.billingService = billingService;
        this.mailService = mailService;
    }

    @GetMapping
    public String page(Model model,
                       @RequestParam(value="msg", required=false) String msg,
                       @RequestParam(value="err", required=false) String err) {

        List<NvtPayment> pendingCash = paymentRepo.findByMethodAndStatusOrderByPaidAtDesc(
                NvtPayment.Method.CASH, NvtPayment.PayStatus.PENDING
        );

        model.addAttribute("pendingCash", pendingCash);
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "admin/cash-payments";
    }

    @PostMapping("/{paymentId}/confirm")
    public String confirm(@PathVariable Long paymentId) {
        try {
            NvtPayment p = paymentRepo.findById(paymentId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy payment ID=" + paymentId));

            if (p.getMethod() != NvtPayment.Method.CASH)
                throw new IllegalArgumentException("Payment này không phải CASH");

            if (p.getStatus() != NvtPayment.PayStatus.PENDING)
                throw new IllegalArgumentException("Payment không ở trạng thái PENDING");

            // ✅ xác nhận
            p.setStatus(NvtPayment.PayStatus.SUCCESS);
            paymentRepo.save(p);

            // ✅ cập nhật trạng thái bill nếu đủ tiền (tính theo SUCCESS)
            NvtBill bill = billingService.findBill(p.getBillId());

            BigDecimal sumPaid = paymentRepo.findByBillIdOrderByPaidAtDesc(bill.getId()).stream()
                    .filter(x -> x.getStatus() == NvtPayment.PayStatus.SUCCESS)
                    .map(NvtPayment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (sumPaid.compareTo(bill.getTotalAmount()) >= 0) {
                bill.setStatus(NvtBill.BillStatus.PAID);
                billRepo.save(bill);
            }

            // ✅ gửi mail sau khi admin confirm
            if (p.getPayerEmail() != null && !p.getPayerEmail().isBlank()) {
                mailService.sendPaymentSuccessEmail(
                        p.getPayerEmail(),
                        "Xác nhận thanh toán tiền mặt",
                        bill,
                        p.getAmount()
                );
            }

            return "redirect:/admin/cash-payments?msg=" + enc("Đã xác nhận tiền mặt và gửi email");
        } catch (Exception e) {
            return "redirect:/admin/cash-payments?err=" + enc(e.getMessage());
        }
    }

    @PostMapping("/{paymentId}/reject")
    public String reject(@PathVariable Long paymentId,
                         @RequestParam(value="reason", required=false) String reason) {
        try {
            NvtPayment p = paymentRepo.findById(paymentId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy payment ID=" + paymentId));

            if (p.getStatus() != NvtPayment.PayStatus.PENDING)
                throw new IllegalArgumentException("Chỉ từ chối khi PENDING");

            p.setStatus(NvtPayment.PayStatus.FAILED);
            if (reason != null && !reason.trim().isBlank()) {
                p.setTransactionCode((p.getTransactionCode() == null ? "" : p.getTransactionCode()) + " | REJECT: " + reason.trim());
            }
            paymentRepo.save(p);

            return "redirect:/admin/cash-payments?msg=" + enc("Đã từ chối yêu cầu thanh toán");
        } catch (Exception e) {
            return "redirect:/admin/cash-payments?err=" + enc(e.getMessage());
        }
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}
