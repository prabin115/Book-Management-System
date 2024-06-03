package com.example.book.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.book.payloads.BookDto;
import com.example.book.payloads.UserDto;
import com.example.book.services.BookService;
import com.example.book.services.UserService;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @PostMapping("/books/borrow/{bookId}")
    public ResponseEntity<BookDto> borrowBook(@PathVariable long bookId, Authentication authentication){
        String email = authentication.getName();
        UserDto userDto = this.userService.getUserByEmail(email);
        return new ResponseEntity<>(this.bookService.borrowBook(bookId, userDto.getId()), HttpStatus.OK);
    }

    @PostMapping("/books/return/{bookId}")
    public ResponseEntity<BookDto> returnBook(@PathVariable long bookId, Authentication authentication){
        String email = authentication.getName();
        UserDto userDto = this.userService.getUserByEmail(email);
        return new ResponseEntity<>(this.bookService.returnBook(bookId, userDto.getId()), HttpStatus.OK);
    }

    
    //get list of books
    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> getAllBooks(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "5") int size){
        return new ResponseEntity<>(this.bookService.getAllBooks(page, size), HttpStatus.OK);
    }

    //get book by id
    @GetMapping("/books/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable long id){
        return new ResponseEntity<>(this.bookService.getBookById(id), HttpStatus.OK);
    }

    // search by author
    @GetMapping("/books/author")
    public ResponseEntity<List<BookDto>> searchByAuthor(@RequestParam(name = "author") String author){
        return new ResponseEntity<>(this.bookService.searchByAuthor(author), HttpStatus.OK);
    }

    // search by genre
    @GetMapping("/books/genre")
    public ResponseEntity<List<BookDto>> searchByGenre(@RequestParam(name = "genre") String genre){
        return new ResponseEntity<>(this.bookService.searchBookByGenre(genre), HttpStatus.OK);
    }

}
