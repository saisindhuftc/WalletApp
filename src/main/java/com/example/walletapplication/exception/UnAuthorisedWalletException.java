package com.example.walletapplication.exception;

public class UnAuthorisedWalletException extends RuntimeException {
    public UnAuthorisedWalletException(String message) {
        super(message);
    }
}
