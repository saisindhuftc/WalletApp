package com.example.walletapplication.exception;

public class DepositFailedException extends RuntimeException {
    public DepositFailedException(String message) {
        super(message);
    }
}
