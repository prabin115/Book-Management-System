package com.example.book.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.book.payloads.LoginDto;
import com.example.book.payloads.UserDto;
import com.example.book.services.JwtService;
import com.example.book.services.UserService;

@CrossOrigin
@RestController
@RequestMapping("/api/public")
public class PublicController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @PostMapping("/register")
    ResponseEntity <UserDto> registerUser(@RequestBody UserDto userDto){
        return new ResponseEntity<>(this.userService.addUser(userDto), HttpStatus.ACCEPTED);
    }

    @PostMapping("/login")
    ResponseEntity <String> authenticateAndGenerateToken(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        if (authentication.isAuthenticated()) {
            return new ResponseEntity<>(jwtService.generateToken(loginDto.getEmail()), HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}
