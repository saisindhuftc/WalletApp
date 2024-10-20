package com.example.walletapplication.controller;

import com.example.walletapplication.service.IntraWalletTransactionService;
import com.example.walletapplication.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("users/{userId}/wallets/{walletId}")
@CrossOrigin
public class TransactionController {

    @Autowired
    private IntraWalletTransactionService intraWalletTransactionService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable Long userId, @PathVariable Long walletId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
            intraWalletTransactionService.isUserAuthorized(userId, walletId);
            List<Object> transactions = transactionService.getTransactions(walletId, startDate, endDate);
            return ResponseEntity.ok(transactions);
    }
}