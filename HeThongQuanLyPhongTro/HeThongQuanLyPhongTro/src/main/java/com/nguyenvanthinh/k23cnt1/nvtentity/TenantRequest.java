package com.nguyenvanthinh.k23cnt1.nvtentity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class TenantRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String phone;
    private String email;

    private LocalDateTime createdAt;

    // PENDING – chờ duyệt
    // APPROVED – đã duyệt
    // REJECTED – từ chối
    private String status;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public void setRejectReason(String reason) {
    }
}
