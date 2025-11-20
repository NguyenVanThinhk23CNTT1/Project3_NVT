package com.nvtdevmaster.lesson08.repository;

import com.nvtdevmaster.lesson08.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
