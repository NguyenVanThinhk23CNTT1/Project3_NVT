package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtFeedback;
import comk23cnt1.nvt.project3.nvt_service.NvtFeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/admin/feedbacks")
public class NvtAdminFeedbackController {

    private final NvtFeedbackService feedbackService;

    public NvtAdminFeedbackController(NvtFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public String list(Model model,
                       @RequestParam(value="status", required=false) String status,
                       @RequestParam(value="msg", required=false) String msg,
                       @RequestParam(value="err", required=false) String err) {

        NvtFeedback.FeedbackStatus st = null;
        if (status != null && !status.isBlank()) {
            st = NvtFeedback.FeedbackStatus.valueOf(status);
        }

        List<NvtFeedback> feedbacks = feedbackService.findByStatus(st);

        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("statuses", NvtFeedback.FeedbackStatus.values());
        model.addAttribute("status", status);
        model.addAttribute("newCount", feedbackService.countByStatus(NvtFeedback.FeedbackStatus.NEW));
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);

        return "admin/feedbacks";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam("status") String status,
                               @RequestParam(value="adminNote", required=false) String adminNote) {
        try {
            NvtFeedback.FeedbackStatus st = NvtFeedback.FeedbackStatus.valueOf(status);
            feedbackService.updateStatus(id, st, adminNote);
            return "redirect:/admin/feedbacks?msg=" + enc("Đã cập nhật phản ánh #" + id);
        } catch (Exception e) {
            return "redirect:/admin/feedbacks?err=" + enc(e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        try {
            feedbackService.delete(id);
            return "redirect:/admin/feedbacks?msg=" + enc("Đã xóa phản ánh #" + id);
        } catch (Exception e) {
            return "redirect:/admin/feedbacks?err=" + enc(e.getMessage());
        }
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}
