package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtBill;
import comk23cnt1.nvt.project3.nvt_entity.NvtBookingRequest;
import comk23cnt1.nvt.project3.nvt_entity.NvtPayment;
import comk23cnt1.nvt.project3.nvt_entity.NvtRoom;
import comk23cnt1.nvt.project3.nvt_repository.NvtBillRepository;
import comk23cnt1.nvt.project3.nvt_repository.NvtBookingRequestRepository;
import comk23cnt1.nvt.project3.nvt_repository.NvtPaymentRepository;
import comk23cnt1.nvt.project3.nvt_repository.NvtRoomRepository;
import comk23cnt1.nvt.project3.nvt_service.NvtBillingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class NvtUserPageController {

    private final NvtRoomRepository roomRepo;
    private final NvtBillRepository billRepo;
    private final NvtBookingRequestRepository bookingRepo;
    private final NvtBillingService billingService;
    private final NvtPaymentRepository paymentRepo;

    public NvtUserPageController(NvtRoomRepository roomRepo,
                                 NvtBillRepository billRepo,
                                 NvtBookingRequestRepository bookingRepo,
                                 NvtBillingService billingService,
                                 NvtPaymentRepository paymentRepo) {
        this.roomRepo = roomRepo;
        this.billRepo = billRepo;
        this.bookingRepo = bookingRepo;
        this.billingService = billingService;
        this.paymentRepo = paymentRepo;
    }

    @GetMapping("/")
    public String home(Model model,
                       @RequestParam(value="msg", required=false) String msg,
                       @RequestParam(value="err", required=false) String err) {
        List<NvtRoom> emptyRooms = roomRepo.findByStatusOrderByFloorAscRoomCodeAsc(NvtRoom.RoomStatus.EMPTY);
        model.addAttribute("rooms", emptyRooms);
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "user/home";
    }

    @GetMapping("/rooms/{id}")
    public String roomDetail(@PathVariable Long id, Model model,
                             @RequestParam(value="msg", required=false) String msg,
                             @RequestParam(value="err", required=false) String err) {
        NvtRoom room = roomRepo.findByIdWithHostel(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng"));

        model.addAttribute("room", room);
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "user/room-detail";
    }

    @PostMapping("/rooms/{id}/book")
    public String book(@PathVariable Long id,
                       @RequestParam String fullName,
                       @RequestParam String phone,
                       @RequestParam(value="note", required=false) String note) {
        try {
            if (fullName == null || fullName.trim().isBlank())
                return "redirect:/rooms/" + id + "?err=" + enc("Họ tên không được rỗng");
            if (phone == null || phone.trim().isBlank())
                return "redirect:/rooms/" + id + "?err=" + enc("SĐT không được rỗng");

            NvtRoom room = roomRepo.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng"));

            if (room.getStatus() != null && room.getStatus() != NvtRoom.RoomStatus.EMPTY) {
                return "redirect:/rooms/" + id + "?err=" + enc("Phòng này hiện không còn trống");
            }

            NvtBookingRequest req = new NvtBookingRequest();
            req.setRoomId(id);
            req.setFullName(fullName.trim());
            req.setPhone(phone.trim());
            req.setNote(note == null || note.trim().isBlank() ? null : note.trim());
            req.setStatus(NvtBookingRequest.Status.NEW);

            bookingRepo.save(req);

            return "redirect:/rooms/" + id + "?msg=" + enc("Đã gửi yêu cầu thuê. Admin sẽ duyệt và liên hệ sớm.");
        } catch (Exception e) {
            return "redirect:/rooms/" + id + "?err=" + enc(e.getMessage());
        }
    }

    @GetMapping("/bills")
    public String billLookup(Model model,
                             @RequestParam(value="roomId", required=false) Long roomId,
                             @RequestParam(value="msg", required=false) String msg,
                             @RequestParam(value="err", required=false) String err) {
        model.addAttribute("rooms", roomRepo.findAll());
        model.addAttribute("roomId", roomId);
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);

        if (roomId != null) {
            List<NvtBill> bills = billRepo.findByRoomIdOrderByBillYearDescBillMonthDesc(roomId);
            model.addAttribute("bills", bills);
        } else {
            model.addAttribute("bills", java.util.Collections.emptyList());
        }
        return "user/bill-lookup";
    }

    @GetMapping("/bills/{billId}")
    public String billDetail(@PathVariable Long billId, Model model,
                             @RequestParam(value="msg", required=false) String msg,
                             @RequestParam(value="err", required=false) String err) {
        NvtBill bill = billingService.findBill(billId);
        model.addAttribute("bill", bill);
        model.addAttribute("payments", paymentRepo.findByBillIdOrderByPaidAtDesc(billId));
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "user/bill-detail";
    }

    // =========================
    //  TIỀN MẶT CHUẨN: PENDING
    // =========================
    @PostMapping("/bills/{billId}/pay-cash")
    public String payCash(@PathVariable Long billId,
                          @RequestParam String email) {
        try {
            if (email == null || email.trim().isBlank()) {
                return "redirect:/bills/" + billId + "?err=" + enc("Email không được rỗng");
            }

            NvtBill bill = billingService.findBill(billId);
            if (bill.getStatus() == NvtBill.BillStatus.PAID) {
                return "redirect:/bills/" + billId + "?msg=" + enc("Hóa đơn đã thanh toán trước đó");
            }

            BigDecimal remain = calcRemain(billId, bill.getTotalAmount());
            if (remain.compareTo(BigDecimal.ZERO) <= 0) {
                return "redirect:/bills/" + billId + "?msg=" + enc("Hóa đơn đã đủ tiền");
            }

            NvtPayment p = new NvtPayment();
            p.setBillId(billId);
            p.setAmount(remain);
            p.setMethod(NvtPayment.Method.CASH);
            p.setStatus(NvtPayment.PayStatus.PENDING);
            p.setPayerEmail(email.trim());
            p.setTransactionCode("CASH-" + System.currentTimeMillis());

            paymentRepo.save(p);

            return "redirect:/bills/" + billId + "?msg=" + enc("Đã gửi yêu cầu thanh toán tiền mặt. Vui lòng chờ Admin xác nhận.");
        } catch (Exception e) {
            return "redirect:/bills/" + billId + "?err=" + enc(e.getMessage());
        }
    }

    // =========================
    //  CHUYỂN KHOẢN: PENDING
    // =========================
    @PostMapping("/bills/{billId}/pay-bank")
    public String payBank(@PathVariable Long billId,
                          @RequestParam String email,
                          @RequestParam String transactionCode) {
        try {
            if (email == null || email.trim().isBlank()) {
                return "redirect:/bills/" + billId + "?err=" + enc("Email không được rỗng");
            }
            if (transactionCode == null || transactionCode.trim().isBlank()) {
                return "redirect:/bills/" + billId + "?err=" + enc("Mã giao dịch không được rỗng");
            }

            NvtBill bill = billingService.findBill(billId);
            if (bill.getStatus() == NvtBill.BillStatus.PAID) {
                return "redirect:/bills/" + billId + "?msg=" + enc("Hóa đơn đã thanh toán trước đó");
            }

            BigDecimal remain = calcRemain(billId, bill.getTotalAmount());
            if (remain.compareTo(BigDecimal.ZERO) <= 0) {
                return "redirect:/bills/" + billId + "?msg=" + enc("Hóa đơn đã đủ tiền");
            }

            NvtPayment p = new NvtPayment();
            p.setBillId(billId);
            p.setAmount(remain);
            p.setMethod(NvtPayment.Method.BANK);
            p.setStatus(NvtPayment.PayStatus.PENDING);
            p.setPayerEmail(email.trim());
            p.setTransactionCode(transactionCode.trim());

            paymentRepo.save(p);

            return "redirect:/bills/" + billId + "?msg=" + enc("Đã gửi yêu cầu chuyển khoản. Vui lòng chờ Admin xác nhận.");
        } catch (Exception e) {
            return "redirect:/bills/" + billId + "?err=" + enc(e.getMessage());
        }
    }

    private BigDecimal calcRemain(Long billId, BigDecimal total) {
        BigDecimal paid = paymentRepo.findByBillIdOrderByPaidAtDesc(billId).stream()
                .filter(p -> p.getStatus() == NvtPayment.PayStatus.SUCCESS)
                .map(NvtPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.subtract(paid);
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}
