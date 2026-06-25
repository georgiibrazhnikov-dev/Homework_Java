package ru.vsu.atm.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) { super(message); }
}