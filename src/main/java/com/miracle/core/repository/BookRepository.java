package com.miracle.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miracle.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
