package com.example.walletapplication.entity;

import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.InvalidAmountException;
import jakarta.persistence.*;

@Entity
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double balance;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Wallet() {
        this.balance = 0.0;
    }

    public Wallet(User user) {
        this.user = user;
    }

    public double deposit(Double amount) {
        if (amount == null || amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than 0");
        }
        this.balance += amount;
        return this.balance;
    }

    public double withdraw(Double amount) {
        if (amount == null || amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than 0");
        }
        if (this.balance < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        this.balance -= amount;
        return this.balance;
    }
}