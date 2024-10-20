package com.example.walletapplication.exception;

public class WithdrawalFailedException extends RuntimeException {
    public WithdrawalFailedException(String message) {
        super(message);
    }
}
