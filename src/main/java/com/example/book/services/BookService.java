package com.example.book.services;

import java.util.List;

import com.example.book.payloads.BookDto;

public interface BookService {
    
    BookDto borrowBook(long bookId, long userId);

    BookDto returnBook(long bookId, long userId);

    List<BookDto> searchByAuthor(String name);

    List<BookDto> searchBookByGenre(String genre);

    List<BookDto> getAllBooks(int page, int size);

    BookDto getBookById(long id);

    BookDto addBook(BookDto bookDto);

    BookDto updateBook(BookDto bookDto, long id);

    void deleteBook(long id);
}
