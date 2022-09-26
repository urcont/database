package com.edu.ulab.app.exception;

public class PersonNotFoundException extends NotFoundException{
    public PersonNotFoundException(Long id) {
        super(String.format("User not found, id: %d", id));
    }
}
