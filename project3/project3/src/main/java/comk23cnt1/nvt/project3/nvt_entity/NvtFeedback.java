package comk23cnt1.nvt.project3.nvt_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="feedbacks")
public class NvtFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="room_id")
    private Long roomId; // có thể null nếu phản ánh chung

    @Column(name="tenant_id")
    private Long tenantId; // có thể null nếu gửi ẩn danh / chưa login

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private FeedbackStatus status;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    public enum FeedbackStatus { NEW, IN_PROGRESS, DONE }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = FeedbackStatus.NEW;
        if (title != null) title = title.trim();
        if (content != null) content = content.trim();
    }
}
