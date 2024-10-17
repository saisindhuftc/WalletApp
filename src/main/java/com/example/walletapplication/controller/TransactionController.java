package com.example.walletapplication.controller;


import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.enums.TransactionType;
import com.example.walletapplication.exception.UnAuthorisedUserException;
import com.example.walletapplication.exception.UnAuthorisedWalletException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.requestDTO.InterTransactionRequestDTO;
import com.example.walletapplication.service.TransactionService;
import com.example.walletapplication.service.UserService;
import com.example.walletapplication.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("users/{userId}/wallets/{walletId}")
@CrossOrigin
public class TransactionController {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private WalletService walletService;

    @PostMapping("/transfers")
    public ResponseEntity<?> transfer(@PathVariable Long userId, @PathVariable Long walletId, @RequestBody InterTransactionRequestDTO interTransactionRequestDTO) {
        try {
            walletService.isUserAuthorized(userId, walletId);
            Double amount = interTransactionRequestDTO.getAmount();
            Long senderId = interTransactionRequestDTO.getSenderWalletId();
            Long receiverId = interTransactionRequestDTO.getReceiverWalletId();
            CurrencyType currency = interTransactionRequestDTO.getCurrency();

            transactionService.transfer(senderId, receiverId, amount, currency);
            return ResponseEntity.ok("Transfer successful. Amount: " + amount);
        } catch (UnAuthorisedUserException e) {
            return ResponseEntity.status(403).body("User not authorized");
        } catch (UnAuthorisedWalletException e) {
            return ResponseEntity.status(403).body("User not authorized for this Wallet");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/history/{type}")
    public ResponseEntity<?> getTransactionHistoryByType(@PathVariable Long userId, @PathVariable Long walletId, @PathVariable TransactionType type) {
        try {
            walletService.isUserAuthorized(userId, walletId);
            return ResponseEntity.ok(transactionService.getTransactionHistoryByType(walletId, type));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (UnAuthorisedUserException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (UnAuthorisedWalletException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getTransactionHistory(@PathVariable Long userId, @PathVariable Long walletId, @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        try {
            walletService.isUserAuthorized(userId, walletId);
            List<Object> transactions = transactionService.getTransactionHistory(walletId, startDate, endDate);
            return ResponseEntity.ok(transactions);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (UnAuthorisedUserException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (UnAuthorisedWalletException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

}