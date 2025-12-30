package comk23cnt1.nvt.project3.nvt_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "feedbacks")
public class NvtFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id")
    private Long roomId; // có thể null

    @Column(name = "tenant_id")
    private Long tenantId; // có thể null

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private FeedbackStatus status;

    @Column(name = "admin_note", columnDefinition = "TEXT")
    private String adminNote; // ghi chú nội bộ của admin

    @Column(name = "admin_reply", columnDefinition = "TEXT")
    private String adminReply; // câu trả lời của admin cho khách hàng

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum FeedbackStatus {
        NEW, IN_PROGRESS, DONE
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null)
            createdAt = now;
        updatedAt = now;

        if (status == null)
            status = FeedbackStatus.NEW;
        if (title != null)
            title = title.trim();
        if (content != null)
            content = content.trim();
        if (adminNote != null)
            adminNote = adminNote.trim();
        if (adminReply != null)
            adminReply = adminReply.trim();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        if (title != null)
            title = title.trim();
        if (content != null)
            content = content.trim();
        if (adminNote != null)
            adminNote = adminNote.trim();
        if (adminReply != null)
            adminReply = adminReply.trim();
    }
}
