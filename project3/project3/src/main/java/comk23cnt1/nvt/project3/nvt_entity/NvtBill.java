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
@Table(
        name = "bills",
        uniqueConstraints = @UniqueConstraint(name = "uq_bill_contract_month_year", columnNames = {"contract_id", "bill_month", "bill_year"})
)
public class NvtBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="contract_id", nullable = false)
    private Long contractId;

    @Column(name="room_id", nullable = false)
    private Long roomId;

    @Column(name="bill_month", nullable = false)
    private Integer billMonth;

    @Column(name="bill_year", nullable = false)
    private Integer billYear;

    @Column(name="rent_amount", nullable = false)
    private BigDecimal rentAmount;

    @Column(name="electric_amount", nullable = false)
    private BigDecimal electricAmount;

    @Column(name="water_amount", nullable = false)
    private BigDecimal waterAmount;

    @Column(name="service_amount", nullable = false)
    private BigDecimal serviceAmount;

    @Column(nullable = false)
    private BigDecimal discount;

    @Column(name="total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name="due_date")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private BillStatus status;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    public enum BillStatus { UNPAID, PAID, OVERDUE }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (rentAmount == null) rentAmount = BigDecimal.ZERO;
        if (electricAmount == null) electricAmount = BigDecimal.ZERO;
        if (waterAmount == null) waterAmount = BigDecimal.ZERO;
        if (serviceAmount == null) serviceAmount = BigDecimal.ZERO;
        if (discount == null) discount = BigDecimal.ZERO;
        if (totalAmount == null) totalAmount = BigDecimal.ZERO;
        if (status == null) status = BillStatus.UNPAID;
    }
}
