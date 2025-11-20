package com.nvtdevmaster.lesson08.repository;

import com.nvtdevmaster.lesson08.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
}
