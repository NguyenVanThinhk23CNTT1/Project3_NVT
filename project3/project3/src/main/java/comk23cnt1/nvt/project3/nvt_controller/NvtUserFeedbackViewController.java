package comk23cnt1.nvt.project3.nvt_controller;

import comk23cnt1.nvt.project3.nvt_entity.NvtFeedback;
import comk23cnt1.nvt.project3.nvt_repository.NvtFeedbackRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller cho user xem feedback của mình
 */
@Controller
@RequestMapping("/user/feedbacks")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class NvtUserFeedbackViewController {

    private final NvtFeedbackRepository feedbackRepository;

    public NvtUserFeedbackViewController(NvtFeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    /**
     * Danh sách feedback của user (có thể lọc theo email hoặc phone)
     */
    @GetMapping
    public String myFeedbacks(Model model,
            @RequestParam(value = "search", required = false) String search) {
        List<NvtFeedback> feedbacks;

        if (search != null && !search.isBlank()) {
            // Tìm theo title hoặc content
            feedbacks = feedbackRepository.findByTitleContainingOrContentContainingOrderByIdDesc(search, search);
        } else {
            // Hiển thị tất cả
            feedbacks = feedbackRepository.findAllByOrderByIdDesc();
        }

        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("search", search);
        return "user/my-feedbacks";
    }

    /**
     * Xem chi tiết 1 feedback
     */
    @GetMapping("/{id}")
    public String viewFeedback(@PathVariable Long id, Model model) {
        NvtFeedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phản ánh"));

        model.addAttribute("feedback", feedback);
        return "user/feedback-detail";
    }
}
