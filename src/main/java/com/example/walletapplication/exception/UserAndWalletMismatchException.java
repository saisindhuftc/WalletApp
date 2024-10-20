package com.example.walletapplication.exception;

public class UserAndWalletMismatchException extends RuntimeException {
    public UserAndWalletMismatchException(String message) {
        super(message);
    }
}
