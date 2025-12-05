package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtFeedback;
import comk23cnt1.nvt.project3.nvt_service.NvtFeedbackService;
import comk23cnt1.nvt.project3.nvt_service.NvtRoomService;
import comk23cnt1.nvt.project3.nvt_service.NvtTenantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/admin/feedbacks")
public class NvtAdminFeedbackController {

    private final NvtFeedbackService feedbackService;
    private final NvtRoomService roomService;
    private final NvtTenantService tenantService;

    public NvtAdminFeedbackController(NvtFeedbackService feedbackService,
                                      NvtRoomService roomService,
                                      NvtTenantService tenantService) {
        this.feedbackService = feedbackService;
        this.roomService = roomService;
        this.tenantService = tenantService;
    }

    @GetMapping
    public String list(Model model,
                       @RequestParam(value="msg", required=false) String msg,
                       @RequestParam(value="err", required=false) String err) {
        model.addAttribute("feedbacks", feedbackService.findAll());
        model.addAttribute("statuses", NvtFeedback.FeedbackStatus.values());
        model.addAttribute("rooms", roomService.findAll());
        model.addAttribute("tenants", tenantService.findAll());
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "admin/feedbacks";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam NvtFeedback.FeedbackStatus status) {
        try {
            feedbackService.updateStatus(id, status);
            return "redirect:/admin/feedbacks?msg=" + enc("Cập nhật trạng thái thành công");
        } catch (Exception e) {
            return "redirect:/admin/feedbacks?err=" + enc(e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        try {
            feedbackService.delete(id);
            return "redirect:/admin/feedbacks?msg=" + enc("Xóa thành công");
        } catch (Exception e) {
            return "redirect:/admin/feedbacks?err=" + enc(e.getMessage());
        }
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}
