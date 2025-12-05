package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtFeedback;
import comk23cnt1.nvt.project3.nvt_service.NvtFeedbackService;
import comk23cnt1.nvt.project3.nvt_service.NvtRoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/feedback")
public class NvtUserFeedbackController {

    private final NvtFeedbackService feedbackService;
    private final NvtRoomService roomService;

    public NvtUserFeedbackController(NvtFeedbackService feedbackService, NvtRoomService roomService) {
        this.feedbackService = feedbackService;
        this.roomService = roomService;
    }

    @GetMapping
    public String form(Model model,
                       @RequestParam(value="msg", required=false) String msg,
                       @RequestParam(value="err", required=false) String err) {
        model.addAttribute("rooms", roomService.findAll());
        model.addAttribute("newFeedback", new NvtFeedback());
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "user/feedback";
    }

    @PostMapping("/send")
    public String send(@ModelAttribute("newFeedback") NvtFeedback f) {
        try {
            feedbackService.create(f);
            return "redirect:/feedback?msg=" + enc("Gửi phản ánh thành công");
        } catch (Exception e) {
            return "redirect:/feedback?err=" + enc(e.getMessage());
        }
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}
