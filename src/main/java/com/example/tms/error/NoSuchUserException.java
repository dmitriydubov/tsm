package com.example.tms.error;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException(String message) {
        super(message);
    }
}
