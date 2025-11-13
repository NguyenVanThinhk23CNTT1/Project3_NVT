package com.nvtdevmaster.lesson06.repository;

import com.nvtdevmaster.lesson06.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
