package com.nguyenvanthinh.k23cnt1.nvtentity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String phone;
    private String email;
    private String citizenId; // CCCD
}
