package com.example.walletapplication.controller;

import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.InvalidAmountException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/{id}/deposit")
    public ResponseEntity<?> deposit(@PathVariable Long id, @RequestParam Double amount) {
        try {
            walletService.deposit(id, amount);
            return ResponseEntity.ok("Deposit successful");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (InvalidAmountException e) {
            return ResponseEntity.status(422).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("{id}/withdrawal")
    public ResponseEntity<String> withdraw(@PathVariable Long id, @RequestParam Double amount) {
        try {
            walletService.withdraw(id, amount);
            return ResponseEntity.ok("Withdrawal successful");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (InvalidAmountException e) {
            return ResponseEntity.status(422).body(e.getMessage());
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam Long fromUserId, @RequestParam Long toUserId, @RequestParam Double amount) {
        try {
            walletService.transfer(fromUserId, toUserId, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (InvalidAmountException e) {
            return ResponseEntity.status(422).body(e.getMessage());
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable Long id) {
        try {
            List<Transaction> transactions = walletService.getTransactionHistory(id);
            return ResponseEntity.ok(transactions);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}