package ru.vsu.atm.exception;

public class UserLoginNotFoundException extends RuntimeException {
    public UserLoginNotFoundException(String message) {
        super(message);
    }
}