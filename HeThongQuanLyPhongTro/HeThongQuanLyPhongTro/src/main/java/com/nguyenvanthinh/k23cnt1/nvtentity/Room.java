package com.nguyenvanthinh.k23cnt1.nvtentity;

import com.nguyenvanthinh.k23cnt1.nvtenums.RoomStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;     // Mã phòng
    private Double price;    // Giá thuê
    private Double area;     // Diện tích
    private String image;    // Ảnh phòng
    private String utilities;// Tiện ích riêng

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status = RoomStatus.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "house_id")
    private House house;     // Thuộc nhà trọ nào
}
