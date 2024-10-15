package com.example.walletapplication.controller;

import com.example.walletapplication.entity.Transaction;
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
            return ResponseEntity.status(404).body(e.getMessage()); // Not Found
        } catch (InvalidAmountException e) {
            return ResponseEntity.status(422).body(e.getMessage()); // Unprocessable Entity
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage()); // Internal Server Error
        }
    }

    @PostMapping("{id}/withdraw")
    public ResponseEntity<String> withdraw(@PathVariable Long id, @RequestParam Double amount) {
        try {
            walletService.withdraw(id, amount);
            return ResponseEntity.ok("Withdrawal successful");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Not Found
        } catch (InvalidAmountException e) {
            return ResponseEntity.status(422).body(e.getMessage()); // Unprocessable Entity
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.status(400).body(e.getMessage()); // Bad Request
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage()); // Internal Server Error
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam Long fromUserId, @RequestParam Long toUserId, @RequestParam Double amount) {
        try {
            walletService.transfer(fromUserId, toUserId, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Not Found
        } catch (InvalidAmountException e) {
            return ResponseEntity.status(422).body(e.getMessage()); // Unprocessable Entity
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.status(400).body(e.getMessage()); // Bad Request
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage()); // Internal Server Error
        }
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable Long id) {
        try {
            List<Transaction> transactions = walletService.getTransactionHistory(id);
            return ResponseEntity.ok(transactions);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(null); // Not Found
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Internal Server Error
        }
    }
}