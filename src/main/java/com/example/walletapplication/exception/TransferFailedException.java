package com.example.walletapplication.exception;

public class TransferFailedException extends RuntimeException {
    public TransferFailedException(String message) {
        super(message);
    }
}
