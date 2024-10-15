package com.example.walletapplication.entity;

import com.example.walletapplication.enums.IntraTransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntraTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int intraWalletTransactionId;

    @Enumerated(EnumType.STRING)
    private IntraTransactionType type;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Wallet wallet;

    private LocalDateTime timestamp;

    public IntraTransaction(IntraTransactionType type, Wallet wallet, LocalDateTime timestamp) {
        this.type = type;
        this.wallet = wallet;
        this.timestamp = timestamp;
    }
}