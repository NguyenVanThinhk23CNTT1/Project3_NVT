package com.nvtdevmaster.lesson08.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imgUrl;

    private Integer quantity;

    private Double price;

    private Boolean isActive;

    // Quan hệ 1-n với bảng trung gian
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductConfig> configs = new ArrayList<>();
}
