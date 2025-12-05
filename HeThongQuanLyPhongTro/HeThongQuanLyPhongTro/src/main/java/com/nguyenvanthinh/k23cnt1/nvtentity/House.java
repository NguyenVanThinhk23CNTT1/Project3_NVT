package com.nguyenvanthinh.k23cnt1.nvtentity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // Tên nhà trọ
    private String address;     // Địa chỉ
    private String phone;       // SĐT chủ trọ / quản lý
    private String image;       // Ảnh đại diện
    private String commonUtilities; // Tiện ích chung (WiFi, camera...)

    @Column(columnDefinition = "TEXT")
    private String description; // Mô tả

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();
}
