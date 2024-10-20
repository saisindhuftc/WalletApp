package com.example.walletapplication.controller;

import com.example.walletapplication.entity.IntraWalletTransaction;
import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.requestDTO.IntraWalletTransactionRequestDTO;
import com.example.walletapplication.service.IntraWalletTransactionService;
import com.example.walletapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("users/{userId}")
public class IntraWalletTransactionController {

    @Autowired
    private UserService userService;

    @Autowired
    private IntraWalletTransactionService walletService;

    @PostMapping("/wallets/{walletId}/deposits")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deposit(@PathVariable Long userId, @PathVariable Long walletId, @RequestBody IntraWalletTransactionRequestDTO depositRequest) {
        walletService.isUserAuthorized(userId, walletId);
        Double amount = depositRequest.getAmount();
        CurrencyType currency = depositRequest.getCurrency();
        walletService.deposit(userId, amount, currency);
        return ResponseEntity.ok(Map.of(
                "message", "Deposit successful",
                "amount", amount,
                "currency", currency
        ));
    }

    @PostMapping("/wallets/{walletId}/withdrawals")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> withdraw(@PathVariable Long userId, @PathVariable Long walletId, @RequestBody IntraWalletTransactionRequestDTO withdrawRequest) {
        walletService.isUserAuthorized(userId, walletId);
        Double amount = withdrawRequest.getAmount();
        CurrencyType currency = withdrawRequest.getCurrency();
        Double withdrawAmount = walletService.withdraw(userId, amount, currency);
        return ResponseEntity.ok(Map.of(
                "message", "Withdrawal successful",
                "withdrawnAmount", withdrawAmount,
                "currency", currency
        ));
    }

    @GetMapping("/wallets/{walletId}/deposits")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<IntraWalletTransaction>> getDeposits(@PathVariable Long userId, @PathVariable Long walletId) {
        walletService.isUserAuthorized(userId, walletId);
        List<IntraWalletTransaction> deposits = walletService.getDeposits(walletId);
        return ResponseEntity.ok(deposits);
    }

    @GetMapping("/wallets/{walletId}/withdrawals")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<IntraWalletTransaction>> getWithdrawals(@PathVariable Long userId, @PathVariable Long walletId) {
        walletService.isUserAuthorized(userId, walletId);
        List<IntraWalletTransaction> withdrawals = walletService.getWithdrawals(walletId);
        return ResponseEntity.ok(withdrawals);
    }
}
