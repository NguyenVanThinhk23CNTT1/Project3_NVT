package com.nguyenvanthinh.k23cnt1.nvtentity;

import com.nguyenvanthinh.k23cnt1.nvtenums.ServiceChargeType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ServicePrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ServiceChargeType type;

    private Double price;
}
