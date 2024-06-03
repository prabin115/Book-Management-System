package com.example.book.services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.book.entities.Author;
import com.example.book.entities.Book;
import com.example.book.entities.User;
import com.example.book.exceptions.BookAlreadyBorrowedException;
import com.example.book.exceptions.ResourceNotFoundException;
import com.example.book.payloads.BookDto;
import com.example.book.repositories.AuthorRepository;
import com.example.book.repositories.BookRepository;
import com.example.book.repositories.UserRepository;
import com.example.book.services.BookService;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<BookDto> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> pagedBooks = this.bookRepository.findAll(pageable);
        List<BookDto> allBooksDto = pagedBooks.stream().map(book-> this.modelMapper.map(book, BookDto.class)).collect(Collectors.toList());
        return allBooksDto;
    }

    @Override
    public BookDto getBookById(long id) {
        Optional<Book> existingBook = this.bookRepository.findById(id);
        if (existingBook.isPresent()) {
            return this.modelMapper.map(existingBook, BookDto.class);
        } else {
            throw new ResourceNotFoundException("Book", id);
        }
    }

    @Override
    public BookDto addBook(BookDto bookDto) {
        
        // Map BookDto to Book entity
        Book book = this.modelMapper.map(bookDto, Book.class);

        // Check if the author already exists based on authorName
        Optional <Author> existingAuthorOptional = this.authorRepository.findByAuthorName(bookDto.getAuthor().getAuthorName());
        Author author;

        if (existingAuthorOptional.isPresent()){
            // Use the existing author
            author = existingAuthorOptional.get();
        } else {
            // Create and save a new author
            author = this.modelMapper.map(bookDto.getAuthor(), Author.class);
            try {
                author = authorRepository.save(author);
            } catch (DataIntegrityViolationException e) {
                // Handle duplicate author name error
                throw new IllegalStateException("Author with name " + bookDto.getAuthor().getAuthorName() + " already exists.");
            }
        }
        book.setAuthor(author);
        Book newBook = this.bookRepository.save(book);
        return this.modelMapper.map(newBook, BookDto.class);
    }

    @Override
    public BookDto updateBook(BookDto bookDto, long id) {
        Optional<Book> existingBookOptional = this.bookRepository.findById(id);
        if (existingBookOptional.isPresent()) {
            Book existingBook = existingBookOptional.get();
            Author author = this.modelMapper.map(bookDto.getAuthor(), Author.class);

            if (author.getId() == null || !authorRepository.existsById(author.getId())) {
                author = authorRepository.save(author);
            }

            existingBook.setTitle(bookDto.getTitle());
            existingBook.setAuthor(author);
            this.bookRepository.save(existingBook);
            return this.modelMapper.map(existingBook, BookDto.class);
        } else {
            throw new ResourceNotFoundException("Book", id);
        }
    }

    @Override
    public void deleteBook(long id) {
        Optional<Book> existingBook = this.bookRepository.findById(id);
        if (existingBook.isPresent()) {
            this.bookRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Book", id);
        }
    }

    @Override
    public List<BookDto> searchBookByGenre(String genre) {
        if (!this.bookRepository.existsByGenre(genre)) {
            throw new IllegalArgumentException("this particular genre does not exist");
        }
        List<Book> books = this.bookRepository.findByGenre(genre);
        List<BookDto> bookDtos = books.stream().map(book-> this.modelMapper.map(book, BookDto.class)).collect(Collectors.toList());
        return bookDtos;
    }
    
    @Override
    public List<BookDto> searchByAuthor(String authorName){
        if (!this.authorRepository.existsByAuthorName(authorName)) {
            throw new IllegalArgumentException("this particular author does not exist");
        }
        Author author = this.authorRepository.findByAuthorName(authorName).get();
        List<Book> books = this.bookRepository.findByAuthor(author);
        List<BookDto> bookDtos = books.stream().map(book-> this.modelMapper.map(book, BookDto.class)).collect(Collectors.toList());
        return bookDtos;
    }

    @Override
    public BookDto borrowBook(long bookId, long userId) {
        Optional<Book> bookOptional = this.bookRepository.findById(bookId);
        Optional<User> userOptional = this.userRepository.findById(userId);
        
        if (bookOptional.isPresent() && userOptional.isPresent()) {
            Book book = bookOptional.get();
            User user = userOptional.get();

            if (book.getBorrowedBy() == null) {
                book.setBorrowedBy(user);
                this.bookRepository.save(book);
                return this.modelMapper.map(book, BookDto.class);
            } else {
                throw new BookAlreadyBorrowedException("Book is already borrowed by another user");
            }
        } else {
            throw new BookAlreadyBorrowedException("Book or User not found");
        }
    }

    @Override
    public BookDto returnBook(long bookId, long userId) {
        Optional<Book> bookOptional = this.bookRepository.findById(bookId);
        Optional<User> userOptional = this.userRepository.findById(userId);
        
        if (bookOptional.isPresent() && userOptional.isPresent()) {
            Book book = bookOptional.get();

            if (book.getBorrowedBy() != null && book.getBorrowedBy().getId() == (userId)) {
                book.setBorrowedBy(null);
                this.bookRepository.save(book);
                return this.modelMapper.map(book, BookDto.class);
            } else {
                throw new BookAlreadyBorrowedException("Book is not borrowed by this person");
            }
        } else {
            throw new BookAlreadyBorrowedException("Book or User not found");
        }
    }
}
