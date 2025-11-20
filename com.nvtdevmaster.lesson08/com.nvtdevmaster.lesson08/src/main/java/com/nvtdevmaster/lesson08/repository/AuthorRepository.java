package com.nvtdevmaster.lesson08.repository;

import com.nvtdevmaster.lesson08.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
