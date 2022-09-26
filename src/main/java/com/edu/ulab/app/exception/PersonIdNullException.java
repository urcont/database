package com.edu.ulab.app.exception;

public class PersonIdNullException extends NotFoundException{
    public PersonIdNullException(String message) {
        super("User id is null");
    }
}
