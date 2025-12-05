package comk23cnt1.nvt.project3.nvt_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class NvtPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="bill_id", nullable = false)
    private Long billId;

    @Column(name="paid_at")
    private LocalDateTime paidAt;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Method method;

    @Column(name="transaction_code")
    private String transactionCode;

    @Enumerated(EnumType.STRING)
    private PayStatus status;

    public enum Method { CASH, BANK, MOMO, VNPAY }
    public enum PayStatus { SUCCESS, FAILED, PENDING }

    @PrePersist
    public void prePersist() {
        if (paidAt == null) paidAt = LocalDateTime.now();
        if (method == null) method = Method.CASH;
        if (status == null) status = PayStatus.SUCCESS;
        if (amount == null) amount = BigDecimal.ZERO;
    }
}
