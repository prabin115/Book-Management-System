package com.example.book.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.book.payloads.BookDto;
import com.example.book.payloads.UserDto;
import com.example.book.services.BookService;
import com.example.book.services.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    //get all users
    @GetMapping("/users")
    ResponseEntity<List<UserDto>> getAllUsers(){
        return new ResponseEntity<>(this.userService.getAllUsers(), HttpStatus.OK);
    }

    //get particular user by id
    @GetMapping("/users/{id}")
    ResponseEntity<UserDto> getUserById(@PathVariable long id){
        return new ResponseEntity<>(this.userService.getUserById(id), HttpStatus.OK);
    }

    //update user
    @PutMapping("/users/{id}")
    ResponseEntity<UserDto> updateUser(@PathVariable long id, @RequestBody UserDto userDetails){
        return new ResponseEntity<>(this.userService.updateUser(id, userDetails), HttpStatus.OK);
    }

    //delete user
    @DeleteMapping("users/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable long id){
        return ResponseEntity.noContent().build();
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

    //add new book
    @PostMapping("/books")
    public ResponseEntity<BookDto> addBook(@RequestBody BookDto bookDto){
        return new ResponseEntity<>(this.bookService.addBook(bookDto), HttpStatus.CREATED);
    }

    //update a book
    @PutMapping("/books/{id}")
    public ResponseEntity<BookDto> updateBook(@RequestBody BookDto bookDto, @PathVariable long id){
        return new ResponseEntity<>(this.bookService.updateBook(bookDto, id), HttpStatus.ACCEPTED);
    }

    //delete a book
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id){
        this.bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
