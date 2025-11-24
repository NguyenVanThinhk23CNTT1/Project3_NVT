package com.nvtdevmaster.lab08.repository;

import com.nvtdevmaster.lab08.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {}
