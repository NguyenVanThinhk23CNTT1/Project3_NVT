package com.nvtdevmaster.lesson08.repository;

import com.nvtdevmaster.lesson08.entity.ProductConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductConfigRepository extends JpaRepository<ProductConfig, Long> {
}
