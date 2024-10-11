package com.example.walletapplication.Controller;

import com.example.walletapplication.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam String username, @RequestParam Double amount) {
        try {
            walletService.deposit(username, amount);
            return ResponseEntity.ok("Deposit successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam String username, @RequestParam Double amount) {
        try {
            walletService.withdraw(username, amount);
            return ResponseEntity.ok("Withdrawal successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
