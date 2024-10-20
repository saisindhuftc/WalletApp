package com.example.walletapplication.controller;

import com.example.walletapplication.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("users/{userId}/wallets/{walletId}")
@CrossOrigin
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transactions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getTransactions(@PathVariable Long userId, @PathVariable Long walletId,
                                             @RequestParam(required = false) String sortBy,
                                             @RequestParam(required = false) String sortOrder,
                                             @RequestParam(required = false) String transactionType) {
        List<Object> transactions = transactionService.getTransactions(userId, walletId, sortBy, sortOrder, transactionType);
        return ResponseEntity.ok(transactions);
    }
}