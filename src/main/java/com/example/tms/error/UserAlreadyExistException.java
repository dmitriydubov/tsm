package com.example.tms.error;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
