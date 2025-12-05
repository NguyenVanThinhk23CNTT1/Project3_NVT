package com.nguyenvanthinh.k23cnt1.nvtentity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int month;
    private int year;

    private double oldElectric;
    private double newElectric;
    private double totalAmount;

    private boolean paid = false;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
