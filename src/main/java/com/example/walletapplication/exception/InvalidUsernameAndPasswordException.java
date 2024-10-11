package com.example.walletapplication.exception;

public class InvalidUsernameAndPasswordException extends RuntimeException {
    public InvalidUsernameAndPasswordException(String message) {
        super(message);
    }
}
