package comk23cnt1.nvt.project3.nvt_service;

import comk23cnt1.nvt.project3.nvt_entity.NvtFeedback;

import java.util.List;

public interface NvtFeedbackService {
    List<NvtFeedback> findAll();
    NvtFeedback findById(Long id);
    NvtFeedback create(NvtFeedback feedback);
    NvtFeedback updateStatus(Long id, NvtFeedback.FeedbackStatus status);
    void delete(Long id);
}
