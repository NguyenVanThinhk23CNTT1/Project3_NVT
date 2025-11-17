package com.nvtdevmaster.lesson07.repository;

import com.nvtdevmaster.lesson07.entity.Category;
import
        org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CategoryRepository extends
        JpaRepository<Category, Long> {
}
