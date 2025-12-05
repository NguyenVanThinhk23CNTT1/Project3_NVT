package comk23cnt1.nvt.project3.nvt_service.impl;

import comk23cnt1.nvt.project3.nvt_entity.NvtFeedback;
import comk23cnt1.nvt.project3.nvt_repository.NvtFeedbackRepository;
import comk23cnt1.nvt.project3.nvt_service.NvtFeedbackService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NvtFeedbackServiceImpl implements NvtFeedbackService {

    private final NvtFeedbackRepository repo;

    public NvtFeedbackServiceImpl(NvtFeedbackRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<NvtFeedback> findAll() {
        return repo.findAll();
    }

    @Override
    public NvtFeedback findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phản ánh ID=" + id));
    }

    @Override
    public NvtFeedback create(NvtFeedback f) {
        if (f.getTitle() == null || f.getTitle().trim().isBlank())
            throw new IllegalArgumentException("Tiêu đề không được rỗng");
        if (f.getContent() == null || f.getContent().trim().isBlank())
            throw new IllegalArgumentException("Nội dung không được rỗng");

        f.setTitle(f.getTitle().trim());
        f.setContent(f.getContent().trim());
        if (f.getStatus() == null) f.setStatus(NvtFeedback.FeedbackStatus.NEW);

        return repo.save(f);
    }

    @Override
    public NvtFeedback updateStatus(Long id, NvtFeedback.FeedbackStatus status) {
        NvtFeedback f = findById(id);
        f.setStatus(status == null ? NvtFeedback.FeedbackStatus.NEW : status);
        return repo.save(f);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new IllegalArgumentException("Không tìm thấy phản ánh để xóa: ID=" + id);
        repo.deleteById(id);
    }
}
