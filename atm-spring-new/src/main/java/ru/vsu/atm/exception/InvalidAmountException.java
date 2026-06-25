package ru.vsu.atm.exception;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(String message) { super(message); }
}