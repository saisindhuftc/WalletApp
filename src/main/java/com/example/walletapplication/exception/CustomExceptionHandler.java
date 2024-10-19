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
        return ResponseEntity.badRequest().body(Map.of(
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
        return ResponseEntity.badRequest().body(Map.of(
                "message", e.getMessage(),
                "status", "Invalid request"
        ));
    }

    //w-d
}
