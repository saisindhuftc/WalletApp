package com.example.walletapplication.controller;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.exception.*;
import com.example.walletapplication.requestDTO.WalletRequestDTO;
import com.example.walletapplication.service.UserService;
import com.example.walletapplication.service.IntraTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users/{userId}/wallets/{walletId}")
public class IntraTransactionController {

    @Autowired
    private UserService userService;

    @Autowired
    private IntraTransactionService walletService;

    @PostMapping("/deposits")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deposit(@PathVariable Long userId, @PathVariable Long walletId, @RequestBody WalletRequestDTO depositRequestDTO) {
        try {
            walletService.isUserAuthorized(userId, walletId);
            Double amount = depositRequestDTO.getAmount();
            CurrencyType currency = depositRequestDTO.getCurrency();
            walletService.deposit(userId, amount, currency);
            return ResponseEntity.ok(amount);
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

    @PostMapping("/withdrawls")
    public ResponseEntity<?> withdraw(@PathVariable Long userId, @PathVariable Long walletId, @RequestBody WalletRequestDTO withdrawRequestDTO) {
        try {
            walletService.isUserAuthorized(userId, walletId);
            Double amount = withdrawRequestDTO.getAmount();
            CurrencyType currency = withdrawRequestDTO.getCurrency();
            Double withdrawAmount = walletService.withdraw(userId, amount, currency);
            return ResponseEntity.ok(withdrawAmount);
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
