package com.example.walletapplication.entity;

import com.example.walletapplication.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntraWalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private double amount;

    private LocalDateTime timestamp;

    public IntraWalletTransaction(Wallet wallet, TransactionType type, Double amount, LocalDateTime timestamp) {
        this.wallet = wallet;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }
}