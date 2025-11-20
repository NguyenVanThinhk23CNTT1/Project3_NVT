package com.nvtdevmaster.lesson08.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value; // VD: 8GB, Core i7...

    // Quan hệ với Product
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    // Quan hệ với Configuration
    @ManyToOne
    @JoinColumn(name = "configId")
    private Configuration configuration;
}
