package com.example.book.payloads;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private long id;
    private String title;
    private String genre;
    private AuthorDto author;
    private UserDto borrowedBy;
}
