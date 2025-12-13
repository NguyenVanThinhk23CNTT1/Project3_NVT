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

    // ✅ NEW: email để admin xác nhận xong gửi biên nhận
    @Column(name="payer_email", length = 120)
    private String payerEmail;

    public enum Method { CASH, BANK, MOMO, ZALOPAY, VNPAY }
    public enum PayStatus { SUCCESS, FAILED, PENDING }

    @PrePersist
    public void prePersist() {
        if (paidAt == null) paidAt = LocalDateTime.now();
        if (method == null) method = Method.CASH;

        // ✅ “chuẩn”: user gửi yêu cầu -> PENDING
        if (status == null) status = PayStatus.PENDING;

        if (amount == null) amount = BigDecimal.ZERO;
        normalize();
    }

    @PreUpdate
    public void preUpdate() {
        normalize();
    }

    private void normalize() {
        if (transactionCode != null) {
            transactionCode = transactionCode.trim();
            if (transactionCode.isBlank()) transactionCode = null;
        }
        if (payerEmail != null) {
            payerEmail = payerEmail.trim();
            if (payerEmail.isBlank()) payerEmail = null;
        }
    }
}
