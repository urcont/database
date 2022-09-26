package com.edu.ulab.app.exception;

public class BookNotFoundException extends NotFoundException{
    public BookNotFoundException(Long id) {
        super(String.format("Book not found, id: %d", id));
    }
}
