package com.nvtdevmaster.lesson08.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean isActive;

    @OneToMany(mappedBy = "configuration", cascade = CascadeType.ALL)
    private List<ProductConfig> productConfigs = new ArrayList<>();
}
