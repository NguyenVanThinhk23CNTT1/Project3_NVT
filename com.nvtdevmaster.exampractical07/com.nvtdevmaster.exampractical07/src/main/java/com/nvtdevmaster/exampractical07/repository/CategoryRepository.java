package com.nvtdevmaster.exampractical07.repository;

import com.nvtdevmaster.exampractical07.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
