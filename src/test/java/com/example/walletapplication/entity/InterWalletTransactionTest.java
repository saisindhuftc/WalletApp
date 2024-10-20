package com.example.walletapplication.entity;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InterWalletTransactionTest {

    @Test
    void testInterTransactionInitialization() {
        Wallet senderWallet = new Wallet(CurrencyType.USD);
        Wallet receiverWallet = new Wallet(CurrencyType.USD);
        double amount = 100.0;
        LocalDateTime timestamp = LocalDateTime.now();

        InterWalletTransaction transaction = new InterWalletTransaction(senderWallet, receiverWallet, TransactionType.TRANSFER, amount, timestamp);

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

        InterWalletTransaction transaction = new InterWalletTransaction(senderWallet, receiverWallet, TransactionType.TRANSFER, amount, LocalDateTime.now());

        assertEquals(amount, transaction.getAmount());
    }

    @Test
    void testInterTransactionSenderReceiverNotNull() {
        Wallet senderWallet = new Wallet(CurrencyType.USD);
        Wallet receiverWallet = new Wallet(CurrencyType.USD);

        InterWalletTransaction transaction = new InterWalletTransaction(senderWallet, receiverWallet, TransactionType.TRANSFER, 100.0, LocalDateTime.now());

        assertNotNull(transaction.getSenderWallet());
        assertNotNull(transaction.getReceiverWallet());
    }

    @Test
    void testInterTransactionTimestamp() {
        Wallet senderWallet = new Wallet(CurrencyType.USD);
        Wallet receiverWallet = new Wallet(CurrencyType.USD);
        LocalDateTime timestamp = LocalDateTime.now();

        InterWalletTransaction transaction = new InterWalletTransaction(senderWallet, receiverWallet, TransactionType.TRANSFER, 100.0, timestamp);

        assertEquals(timestamp, transaction.getTimestamp());
    }

    @Test
    void testInterTransactionTransactionType() {
        Wallet senderWallet = new Wallet(CurrencyType.USD);
        Wallet receiverWallet = new Wallet(CurrencyType.USD);

        InterWalletTransaction transaction = new InterWalletTransaction(senderWallet, receiverWallet, TransactionType.TRANSFER, 100.0, LocalDateTime.now());

        assertEquals(TransactionType.TRANSFER, transaction.getTransactionType());
    }
}