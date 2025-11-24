package com.nvtdevmaster.lab08.repository;

import com.nvtdevmaster.lab08.entity.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {}
