package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtBookingRequest;
import comk23cnt1.nvt.project3.nvt_service.NvtBookingRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/admin/booking-requests")
public class NvtAdminBookingRequestController {

    private final NvtBookingRequestService bookingService;

    public NvtAdminBookingRequestController(NvtBookingRequestService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public String list(Model model,
                       @RequestParam(value="status", required=false) String status,
                       @RequestParam(value="msg", required=false) String msg,
                       @RequestParam(value="err", required=false) String err) {

        NvtBookingRequest.Status st = null;
        if (status != null && !status.isBlank()) st = NvtBookingRequest.Status.valueOf(status);

        List<NvtBookingRequest> requests = bookingService.findByStatus(st);

        model.addAttribute("requests", requests);
        model.addAttribute("statuses", NvtBookingRequest.Status.values());
        model.addAttribute("status", status);
        model.addAttribute("newCount", bookingService.countByStatus(NvtBookingRequest.Status.NEW));
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);

        return "admin/booking-requests";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam("status") String status,
                               @RequestParam(value="adminNote", required=false) String adminNote) {
        try {
            NvtBookingRequest.Status st = NvtBookingRequest.Status.valueOf(status);
            bookingService.updateStatus(id, st, adminNote);
            return "redirect:/admin/booking-requests?msg=" + enc("Đã cập nhật yêu cầu #" + id);
        } catch (Exception e) {
            return "redirect:/admin/booking-requests?err=" + enc(e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        try {
            bookingService.delete(id);
            return "redirect:/admin/booking-requests?msg=" + enc("Đã xóa yêu cầu #" + id);
        } catch (Exception e) {
            return "redirect:/admin/booking-requests?err=" + enc(e.getMessage());
        }
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}
