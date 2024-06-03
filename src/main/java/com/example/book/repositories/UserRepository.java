package com.example.book.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.book.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
