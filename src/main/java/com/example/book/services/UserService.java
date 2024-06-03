package com.example.book.services;

import java.util.List;

import com.example.book.payloads.UserDto;

public interface UserService {
    UserDto getUserByEmail(String email);
    List<UserDto> getAllUsers();
    UserDto getUserById(long id);
    UserDto addUser(UserDto userDto);
    UserDto updateUser(long id,UserDto  userDto);
    void deleteUser(long id);
}
