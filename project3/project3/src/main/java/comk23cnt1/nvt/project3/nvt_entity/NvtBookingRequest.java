package comk23cnt1.nvt.project3.nvt_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "booking_requests")
public class NvtBookingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="room_id", nullable = false)
    private Long roomId;

    @Column(name="full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name="phone", nullable = false, length = 30)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Status status;

    @Column(name="admin_note", columnDefinition = "TEXT")
    private String adminNote;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    public enum Status { NEW, CONTACTED, APPROVED, REJECTED }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        updatedAt = now;

        if (status == null) status = Status.NEW;
        if (fullName != null) fullName = fullName.trim();
        if (phone != null) phone = phone.trim();
        if (note != null) note = note.trim();
        if (adminNote != null) adminNote = adminNote.trim();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        if (adminNote != null) adminNote = adminNote.trim();
    }
}
