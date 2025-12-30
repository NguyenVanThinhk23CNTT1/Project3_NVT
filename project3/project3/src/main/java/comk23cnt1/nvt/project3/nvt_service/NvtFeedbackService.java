package comk23cnt1.nvt.project3.nvt_service;

import comk23cnt1.nvt.project3.nvt_entity.NvtFeedback;

import java.util.List;

public interface NvtFeedbackService {
    List<NvtFeedback> findAll();

    List<NvtFeedback> findByStatus(NvtFeedback.FeedbackStatus status);

    long countByStatus(NvtFeedback.FeedbackStatus status);

    NvtFeedback findById(Long id);

    NvtFeedback create(NvtFeedback f);

    NvtFeedback updateStatus(Long id, NvtFeedback.FeedbackStatus status, String adminNote, String adminReply);

    void delete(Long id);
}
