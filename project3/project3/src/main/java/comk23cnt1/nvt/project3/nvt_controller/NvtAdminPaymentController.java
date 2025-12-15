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

    // ✅ Trang duyệt chung CASH + BANK + MOMO + ZALOPAY
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

        List<NvtPayment> pendingMomo =
                paymentRepo.findByMethodAndStatusOrderByPaidAtDesc(
                        NvtPayment.Method.MOMO,
                        NvtPayment.PayStatus.PENDING
                );

        List<NvtPayment> pendingZaloPay =
                paymentRepo.findByMethodAndStatusOrderByPaidAtDesc(
                        NvtPayment.Method.ZALOPAY,
                        NvtPayment.PayStatus.PENDING
                );

        model.addAttribute("pendingCash", pendingCash);
        model.addAttribute("pendingBank", pendingBank);
        model.addAttribute("pendingMomo", pendingMomo);
        model.addAttribute("pendingZaloPay", pendingZaloPay);
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);

        return "admin/cash-payments";
    }

    // ✅ DUYỆT (CASH/BANK/MOMO/ZALOPAY đều dùng chung) -> SUCCESS + cập nhật bill + gửi mail
    @PostMapping("/cash-payments/{paymentId}/confirm")
    public String confirm(@PathVariable Long paymentId) {
        try {
            NvtPayment p = paymentRepo.findById(paymentId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy payment ID=" + paymentId));

            if (p.getStatus() != NvtPayment.PayStatus.PENDING)
                throw new IllegalArgumentException("Payment không ở trạng thái PENDING");

            // confirm
            p.setStatus(NvtPayment.PayStatus.SUCCESS);
            p.setPaidAt(LocalDateTime.now());
            paymentRepo.save(p);

            // cập nhật bill nếu đủ tiền (tính theo SUCCESS)
            NvtBill bill = billRepo.findById(p.getBillId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bill ID=" + p.getBillId()));

            BigDecimal sumPaid = paymentRepo.findByBillIdOrderByPaidAtDesc(bill.getId()).stream()
                    .filter(x -> x.getStatus() == NvtPayment.PayStatus.SUCCESS)
                    .map(NvtPayment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (sumPaid.compareTo(bill.getTotalAmount()) >= 0) {
                bill.setStatus(NvtBill.BillStatus.PAID);
                billRepo.save(bill);
            }

            // gửi mail sau khi admin confirm
            if (p.getPayerEmail() != null && !p.getPayerEmail().isBlank()) {
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

    // ✅ TỪ CHỐI (nhận reason từ form)
    @PostMapping("/cash-payments/{paymentId}/reject")
    public String reject(@PathVariable Long paymentId,
                         @RequestParam(value = "reason", required = false) String reason) {
        try {
            NvtPayment p = paymentRepo.findById(paymentId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy payment ID=" + paymentId));

            if (p.getStatus() != NvtPayment.PayStatus.PENDING)
                throw new IllegalArgumentException("Chỉ từ chối khi PENDING");

            p.setStatus(NvtPayment.PayStatus.FAILED);

            // gắn lý do vào transactionCode cho dễ xem
            if (reason != null && !reason.trim().isBlank()) {
                String old = (p.getTransactionCode() == null ? "" : p.getTransactionCode());
                p.setTransactionCode(old + " | REJECT: " + reason.trim());
            }

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
