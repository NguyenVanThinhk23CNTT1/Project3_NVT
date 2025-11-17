package com.nvtdevmaster.exampractical07.repository;

import com.nvtdevmaster.exampractical07.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
