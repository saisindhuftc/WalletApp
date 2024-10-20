package com.example.walletapplication.entity;

import com.example.walletapplication.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InterWalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_wallet_id", nullable = false)
    private Wallet senderWallet;

    @ManyToOne
    @JoinColumn(name = "reciever_wallet_id", nullable = false)
    private Wallet receiverWallet;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private double amount;

    private LocalDateTime timestamp;

    public InterWalletTransaction(Wallet senderWallet, Wallet receiverWallet, TransactionType transactionType, double amount, LocalDateTime timestamp) {
        this.senderWallet = senderWallet;
        this.receiverWallet = receiverWallet;
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = timestamp;
    }
}