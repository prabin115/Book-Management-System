package com.example.book.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.book.entities.Author;
import com.example.book.entities.Book;
import java.util.List;


public interface BookRepository extends JpaRepository <Book, Long> {
    boolean existsByGenre(String genre);
    List<Book> findByGenre(String genre);
    List<Book> findByAuthor(Author author);
}
