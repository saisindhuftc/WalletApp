package com.example.walletapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UnAuthorisedUserException.class)
    public ResponseEntity<Map<String, String>> handleUnAuthorisedUserException(UnAuthorisedUserException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "message", e.getMessage(),
                "status", "User not authorized"
        ));
    }

    @ExceptionHandler(UnAuthorisedWalletException.class)
    public ResponseEntity<Map<String, String>> handleUnAuthorisedWalletException(UnAuthorisedWalletException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "message", e.getMessage(),
                "status", "User not authorized for this wallet"
        ));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "message", e.getMessage(),
                "status", "User already exists"
        ));
    }

    @ExceptionHandler(InvalidUsernameAndPasswordException.class)
    public ResponseEntity<Map<String, String>> handleInvalidUsernameAndPasswordException(InvalidUsernameAndPasswordException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "message", e.getMessage(),
                "status", "Invalid credentials"
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "message", e.getMessage(),
                "status", "Invalid request"
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", e.getMessage(),
                "status", "Internal server error"
        ));
    }
    @ExceptionHandler(InvalidCurrencyException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCurrencyException(InvalidCurrencyException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "message", e.getMessage(),
                "status", "Invalid request"
        ));
    }
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientBalanceException(InsufficientBalanceException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "message", e.getMessage(),
                "status", "Insufficient balance"
        ));
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<Map<String, String>> handleAmountIsInvalidException(InvalidAmountException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "message", e.getMessage(),
                "status", "Invalid amount"
        ));
    }

    @ExceptionHandler(DepositFailedException.class)
    public ResponseEntity<Map<String, String>> handleDepositFailedException(DepositFailedException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", e.getMessage(),
                "status", "Deposit failed"
        ));
    }

    @ExceptionHandler(WithdrawalFailedException.class)
    public ResponseEntity<Map<String, String>> handleWithdrawalFailedException(WithdrawalFailedException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", e.getMessage(),
                "status", "Withdrawal failed"
        ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "message", e.getMessage(),
                "status", "User not found"
        ));
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleWalletNotFoundException(WalletNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "message", e.getMessage(),
                "status", "Wallet not found"
        ));
    }

    @ExceptionHandler(UserAndWalletMismatchException.class)
    public ResponseEntity<Map<String, String>> handleUserAndWalletMismatchException(UserAndWalletMismatchException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "message", e.getMessage(),
                "status", "User and wallet mismatch"
        ));
    }

    @ExceptionHandler(TransferFailedException.class)
    public ResponseEntity<Map<String, String>> handleTransferFailedException(TransferFailedException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", e.getMessage(),
                "status", "Transfer failed"
        ));
    }


}
