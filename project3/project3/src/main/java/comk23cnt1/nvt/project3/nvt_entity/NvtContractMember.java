package comk23cnt1.nvt.project3.nvt_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "contract_members")
@IdClass(NvtContractMemberId.class)
public class NvtContractMember {

    @Id
    @Column(name="contract_id")
    private Long contractId;

    @Id
    @Column(name="tenant_id")
    private Long tenantId;

    @Column(length = 50)
    private String relation;

    @Column(name="move_in_date")
    private LocalDate moveInDate;

    @Column(name="move_out_date")
    private LocalDate moveOutDate;

    @Column(name="is_primary", nullable = false)
    private Boolean isPrimary = false;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (isPrimary == null) isPrimary = false;
        normalize();
    }

    @PreUpdate
    public void preUpdate() {
        normalize();
    }

    private void normalize() {
        if (relation != null) {
            relation = relation.trim();
            if (relation.isBlank()) relation = null;
        }
    }
}
