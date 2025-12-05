package comk23cnt1.nvt.project3.nvt_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "contracts")
public class NvtContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="contract_code", nullable = false, unique = true, length = 30)
    private String contractCode;

    @Column(name="room_id", nullable = false)
    private Long roomId;

    @Column(name="tenant_id", nullable = false)
    private Long tenantId; // đại diện

    @Column(name="start_date", nullable = false)
    private LocalDate startDate;

    @Column(name="end_date")
    private LocalDate endDate;

    @Column(nullable = false)
    private BigDecimal deposit;

    @Column(name="rent_price", nullable = false)
    private BigDecimal rentPrice;

    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    public enum ContractStatus { ACTIVE, ENDED, CANCELLED }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = LocalDateTime.now();
        if (status == null) status = ContractStatus.ACTIVE;
        if (deposit == null) deposit = BigDecimal.ZERO;
        normalize();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        normalize();
    }

    private void normalize() {
        if (contractCode != null) contractCode = contractCode.trim();
        if (note != null) note = note.trim();
        if (note != null && note.isBlank()) note = null;
    }
}
