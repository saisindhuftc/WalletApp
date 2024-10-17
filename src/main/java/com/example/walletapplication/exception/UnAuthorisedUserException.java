package com.example.walletapplication.exception;

public class UnAuthorisedUserException extends RuntimeException {
    public UnAuthorisedUserException(String message) {
        super(message);
    }
}
