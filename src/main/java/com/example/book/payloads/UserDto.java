package com.example.book.payloads;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    
    private long id;

    private String name;

    private String email;

    private String password;

    private Set <RoleDto> roles;

    // private Set<BookDto> borrowedBooks;
}
