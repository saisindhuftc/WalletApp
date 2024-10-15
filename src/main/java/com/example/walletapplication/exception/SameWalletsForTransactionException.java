package com.example.walletapplication.exception;

public class SameWalletsForTransactionException extends RuntimeException {
    public SameWalletsForTransactionException(String message) {
        super(message);
    }
}
