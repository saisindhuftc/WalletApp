package com.example.walletapplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int interWalletTransactionId;

    @ManyToOne(cascade = CascadeType.ALL)
    private User sender;

    private int senderWalletId;

    @ManyToOne(cascade = CascadeType.ALL)
    private User receiver;

    private int receiverWalletId;


    @OneToOne(cascade = CascadeType.ALL)
    private IntraTransaction deposit;

    @OneToOne(cascade = CascadeType.ALL)
    private IntraTransaction withdrawal;

    public InterTransaction(User sender, int senderWalletId, User receiver, int receiverWalletId,IntraTransaction deposit, IntraTransaction withdrawal) {
        this.sender = sender;
        this.senderWalletId = senderWalletId;
        this.receiver = receiver;
        this.receiverWalletId = receiverWalletId;
        this.deposit = deposit;
        this.withdrawal = withdrawal;
    }
}