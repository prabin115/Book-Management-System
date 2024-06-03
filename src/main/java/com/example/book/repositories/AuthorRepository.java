package com.example.book.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.book.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByAuthorName(String name);
    boolean existsByAuthorName(String name);
}
