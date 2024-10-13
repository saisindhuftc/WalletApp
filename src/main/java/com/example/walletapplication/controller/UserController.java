package com.example.walletapplication.controller;

import com.example.walletapplication.entity.User;
import com.example.walletapplication.exception.UserAlreadyExistsException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.InvalidAmountException;
import com.example.walletapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String username, @RequestParam String password) {
        try {
            User user = userService.registerUser(username, password);
            return ResponseEntity.ok(user);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // Conflict
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<?> deposit(@PathVariable Long id, @RequestParam Double amount) {
        try {
            Double newBalance = userService.deposit(id, amount);
            return ResponseEntity.ok("Deposit successful. New balance: " + newBalance);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Not Found
        } catch (InvalidAmountException e) {
            return ResponseEntity.status(422).body(e.getMessage()); // Unprocessable Entity
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable Long id, @RequestParam Double amount) {
        try {
            Double newBalance = userService.withdraw(id, amount);
            return ResponseEntity.ok("Withdrawal successful. New balance: " + newBalance);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Not Found
        } catch (InvalidAmountException e) {
            return ResponseEntity.status(422).body(e.getMessage()); // Unprocessable Entity
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.status(400).body(e.getMessage()); // Bad Request
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserByUserId(@PathVariable Long id) {
        User user = userService.getUserByUserId(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}