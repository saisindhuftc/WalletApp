package com.example.walletapplication.controller;


import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.requestDTO.InterWalletTransactionRequestDTO;
import com.example.walletapplication.service.InterWalletTransactionService;
import com.example.walletapplication.service.IntraWalletTransactionService;
import com.example.walletapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("users/{userId}/wallets/{walletId}")
@CrossOrigin
public class InterWalletTransactionController {

    @Autowired
    private UserService userService;

    @Autowired
    private InterWalletTransactionService interWalletTransactionService;
    @Autowired
    private IntraWalletTransactionService intraWalletTransactionService;

    @PostMapping("/transactions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> transfer(@PathVariable Long userId, @PathVariable Long walletId, @RequestBody InterWalletTransactionRequestDTO interTransactionRequest) {

        intraWalletTransactionService.isUserAuthorized(userId, walletId);
        Double amount = interTransactionRequest.getAmount();
        Long senderId = interTransactionRequest.getSenderWalletId();
        Long receiverId = interTransactionRequest.getReceiverWalletId();
        CurrencyType currency = interTransactionRequest.getCurrency();

        interWalletTransactionService.transfer(senderId, receiverId, amount, currency);
        return ResponseEntity.ok(Map.of(
                "message", "Transfer successful",
                "amount", amount
        ));
    }
}