package comk23cnt1.nvt.project3.nvt_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tenants")
public class NvtTenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="full_name", nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(unique = true, length = 20)
    private String cccd;

    private LocalDate birthday;

    @Column(length = 255)
    private String address;

    @Column(name="user_id")
    private Long userId; // để sau nối login user

    @Enumerated(EnumType.STRING)
    private TenantStatus status;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    public enum TenantStatus { ACTIVE, INACTIVE }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = TenantStatus.ACTIVE;
        normalize();
    }

    @PreUpdate
    public void preUpdate() {
        normalize();
        if (this.status == null) this.status = TenantStatus.ACTIVE;
    }

    private void normalize() {
        if (this.fullName != null) this.fullName = this.fullName.trim();
        if (this.phone != null) this.phone = this.phone.trim();
        if (this.cccd != null) this.cccd = this.cccd.trim();
        if (this.address != null) this.address = this.address.trim();
        if (this.cccd != null && this.cccd.isBlank()) this.cccd = null;
        if (this.address != null && this.address.isBlank()) this.address = null;
    }
}
