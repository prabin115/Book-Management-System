package com.example.book.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    String resource;
    long id;

    public ResourceNotFoundException(String resource, long id){
        super(String.format("%s not found with id : %s", resource, id));
        this.id = id;
        this.resource = resource;
    }
}
