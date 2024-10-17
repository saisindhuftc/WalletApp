package com.example.walletapplication.entity;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InterTransactionTest {

    @Test
    void testInterTransactionInitialization() {
        Wallet senderWallet = new Wallet(CurrencyType.USD);
        Wallet receiverWallet = new Wallet(CurrencyType.USD);
        double amount = 100.0;
        LocalDateTime timestamp = LocalDateTime.now();

        InterTransaction transaction = new InterTransaction(senderWallet, receiverWallet, TransactionType.TRANSFER, amount, timestamp);

        assertEquals(senderWallet, transaction.getSenderWallet());
        assertEquals(receiverWallet, transaction.getReceiverWallet());
        assertEquals(TransactionType.TRANSFER, transaction.getTransactionType());
        assertEquals(amount, transaction.getAmount());
        assertEquals(timestamp, transaction.getTimestamp());
    }

    @Test
    void testInterTransactionAmount() {
        Wallet senderWallet = new Wallet(CurrencyType.USD);
        Wallet receiverWallet = new Wallet(CurrencyType.USD);
        double amount = 100.0;

        InterTransaction transaction = new InterTransaction(senderWallet, receiverWallet, TransactionType.TRANSFER, amount, LocalDateTime.now());

        assertEquals(amount, transaction.getAmount());
    }

    @Test
    void testInterTransactionSenderReceiverNotNull() {
        Wallet senderWallet = new Wallet(CurrencyType.USD);
        Wallet receiverWallet = new Wallet(CurrencyType.USD);

        InterTransaction transaction = new InterTransaction(senderWallet, receiverWallet, TransactionType.TRANSFER, 100.0, LocalDateTime.now());

        assertNotNull(transaction.getSenderWallet());
        assertNotNull(transaction.getReceiverWallet());
    }
}
