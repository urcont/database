package com.edu.ulab.app.exception;

public class BookIdNullException extends NotFoundException{
    public BookIdNullException(String message) {
        super("Book id is null");
    }
}
