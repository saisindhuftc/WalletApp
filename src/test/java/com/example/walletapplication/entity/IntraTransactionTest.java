package com.example.walletapplication.entity;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class IntraTransactionTest {

    @Test
    void testIntraTransactionInitialization() {
        Wallet wallet = new Wallet(CurrencyType.USD);
        double amount = 50.0;
        LocalDateTime timestamp = LocalDateTime.now();

        IntraTransaction transaction = new IntraTransaction(wallet, TransactionType.DEPOSIT, amount, timestamp);

        assertEquals(wallet, transaction.getWallet());
        assertEquals(TransactionType.DEPOSIT, transaction.getType());
        assertEquals(amount, transaction.getAmount());
        assertEquals(timestamp, transaction.getTimestamp());
    }

    @Test
    void testIntraTransactionAmount() {
        Wallet wallet = new Wallet(CurrencyType.USD);
        double amount = 75.0;

        IntraTransaction transaction = new IntraTransaction(wallet, TransactionType.WITHDRAWAL, amount, LocalDateTime.now());

        assertEquals(amount, transaction.getAmount());
    }

    @Test
    void testIntraTransactionWalletNotNull() {
        Wallet wallet = new Wallet(CurrencyType.USD);

        IntraTransaction transaction = new IntraTransaction(wallet, TransactionType.DEPOSIT, 100.0, LocalDateTime.now());

        assertNotNull(transaction.getWallet());
    }
}
