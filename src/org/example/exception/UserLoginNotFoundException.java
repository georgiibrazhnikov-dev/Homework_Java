package org.example.exception;

public class UserLoginNotFoundException extends RuntimeException {
    public UserLoginNotFoundException(String message) {
        super(message);
    }
}