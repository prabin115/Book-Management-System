package com.example.book.services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.book.entities.User;
import com.example.book.exceptions.ResourceNotFoundException;
import com.example.book.payloads.UserDto;
import com.example.book.repositories.UserRepository;
import com.example.book.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<UserDto> getAllUsers() {
        List<User> allUsers = this.userRepository.findAll();
        List<UserDto> allUsersDtos = allUsers.stream().map(user-> this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        return allUsersDtos;
    }

    @Override
    public UserDto getUserById(long id) {
        Optional<User> existingUserOptional = this.userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            UserDto userDto = this.modelMapper.map(existingUser, UserDto.class);
            return userDto;
        } else {
            throw new ResourceNotFoundException("User", id);
        }
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        if (this.userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        User newUser = this.modelMapper.map(userDto, User.class);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        this.userRepository.save(newUser);
        return this.modelMapper.map(newUser, UserDto.class);   
    }
    
    public UserDto updateUser(long id, UserDto userDto){
        if (this.userRepository.findById(id).isPresent()) {
            User updatedUser = this.modelMapper.map(userDto, User.class);
            this.userRepository.save(updatedUser);
            return this.modelMapper.map(updatedUser, UserDto.class);
        } else {
            throw new RuntimeException("User does not exist !");
        }
    }

    public void deleteUser(long id){
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email);
        return this.modelMapper.map(user, UserDto.class);
    }
}
