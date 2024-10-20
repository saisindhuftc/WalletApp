package com.example.walletapplication.entity;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class IntraWalletTransactionTest {

    @Test
    void testIntraTransactionInitialization() {
        Wallet wallet = new Wallet(CurrencyType.USD);
        double amount = 50.0;
        LocalDateTime timestamp = LocalDateTime.now();

        IntraWalletTransaction transaction = new IntraWalletTransaction(wallet, TransactionType.DEPOSIT, amount, timestamp);

        assertEquals(wallet, transaction.getWallet());
        assertEquals(TransactionType.DEPOSIT, transaction.getType());
        assertEquals(amount, transaction.getAmount());
        assertEquals(timestamp, transaction.getTimestamp());
    }

    @Test
    void testIntraTransactionAmount() {
        Wallet wallet = new Wallet(CurrencyType.USD);
        double amount = 75.0;

        IntraWalletTransaction transaction = new IntraWalletTransaction(wallet, TransactionType.WITHDRAWAL, amount, LocalDateTime.now());

        assertEquals(amount, transaction.getAmount());
    }

    @Test
    void testIntraTransactionWalletNotNull() {
        Wallet wallet = new Wallet(CurrencyType.USD);

        IntraWalletTransaction transaction = new IntraWalletTransaction(wallet, TransactionType.DEPOSIT, 100.0, LocalDateTime.now());

        assertNotNull(transaction.getWallet());
    }

    @Test
    void testIntraTransactionWithdrawalType() {
        Wallet wallet = new Wallet(CurrencyType.USD);
        double amount = 25.0;
        LocalDateTime timestamp = LocalDateTime.now();

        IntraWalletTransaction transaction = new IntraWalletTransaction(wallet, TransactionType.WITHDRAWAL, amount, timestamp);

        assertEquals(TransactionType.WITHDRAWAL, transaction.getType());
        assertEquals(amount, transaction.getAmount());
        assertEquals(wallet, transaction.getWallet());
    }

    @Test
    void testIntraTransactionDepositType() {
        Wallet wallet = new Wallet(CurrencyType.USD);
        double amount = 100.0;
        LocalDateTime timestamp = LocalDateTime.now();

        IntraWalletTransaction transaction = new IntraWalletTransaction(wallet, TransactionType.DEPOSIT, amount, timestamp);

        assertEquals(TransactionType.DEPOSIT, transaction.getType());
        assertEquals(amount, transaction.getAmount());
        assertEquals(wallet, transaction.getWallet());
    }

    @Test
    void testIntraTransactionNegativeAmount() {
        Wallet wallet = new Wallet(CurrencyType.USD);
        double amount = -50.0;

        IntraWalletTransaction transaction = new IntraWalletTransaction(wallet, TransactionType.WITHDRAWAL, amount, LocalDateTime.now());

        assertEquals(amount, transaction.getAmount());
        assertEquals(TransactionType.WITHDRAWAL, transaction.getType());
        assertEquals(wallet, transaction.getWallet());
    }

    @Test
    void testIntraTransactionTimestampNotNull() {
        Wallet wallet = new Wallet(CurrencyType.USD);
        double amount = 50.0;

        IntraWalletTransaction transaction = new IntraWalletTransaction(wallet, TransactionType.DEPOSIT, amount, LocalDateTime.now());

        assertNotNull(transaction.getTimestamp());
    }

    @Test
    void testIntraTransactionEquals() {
        Wallet wallet = new Wallet(CurrencyType.USD);
        double amount = 100.0;
        LocalDateTime timestamp = LocalDateTime.now();

        IntraWalletTransaction transaction1 = new IntraWalletTransaction(wallet, TransactionType.DEPOSIT, amount, timestamp);
        IntraWalletTransaction transaction2 = new IntraWalletTransaction(wallet, TransactionType.DEPOSIT, amount, timestamp);

        assertEquals(transaction1, transaction2);
    }

    @Test
    void testIntraTransactionNotEqualDifferentType() {
        Wallet wallet = new Wallet(CurrencyType.USD);
        double amount = 100.0;
        LocalDateTime timestamp = LocalDateTime.now();

        IntraWalletTransaction transaction1 = new IntraWalletTransaction(wallet, TransactionType.DEPOSIT, amount, timestamp);
        IntraWalletTransaction transaction2 = new IntraWalletTransaction(wallet, TransactionType.WITHDRAWAL, amount, timestamp);

        assertNotEquals(transaction1, transaction2);
    }

    @Test
    void testIntraTransactionNotEqualDifferentWallet() {
        Wallet wallet1 = new Wallet(CurrencyType.USD);
        Wallet wallet2 = new Wallet(CurrencyType.EUR);
        double amount = 100.0;
        LocalDateTime timestamp = LocalDateTime.now();

        IntraWalletTransaction transaction1 = new IntraWalletTransaction(wallet1, TransactionType.DEPOSIT, amount, timestamp);
        IntraWalletTransaction transaction2 = new IntraWalletTransaction(wallet2, TransactionType.DEPOSIT, amount, timestamp);

        assertNotEquals(transaction1, transaction2);
    }

    @Test
    void testIntraTransactionNotEqualDifferentAmount() {
        Wallet wallet = new Wallet(CurrencyType.USD);
        double amount1 = 100.0;
        double amount2 = 200.0;
        LocalDateTime timestamp = LocalDateTime.now();

        IntraWalletTransaction transaction1 = new IntraWalletTransaction(wallet, TransactionType.DEPOSIT, amount1, timestamp);
        IntraWalletTransaction transaction2 = new IntraWalletTransaction(wallet, TransactionType.DEPOSIT, amount2, timestamp);

        assertNotEquals(transaction1, transaction2);
    }

    @Test
    void testIntraTransactionNotEqualDifferentTimestamp() {
        Wallet wallet = new Wallet(CurrencyType.USD);
        double amount = 100.0;
        LocalDateTime timestamp1 = LocalDateTime.now();
        LocalDateTime timestamp2 = timestamp1.plusHours(1);

        IntraWalletTransaction transaction1 = new IntraWalletTransaction(wallet, TransactionType.DEPOSIT, amount, timestamp1);
        IntraWalletTransaction transaction2 = new IntraWalletTransaction(wallet, TransactionType.DEPOSIT, amount, timestamp2);

        assertNotEquals(transaction1, transaction2);
    }
}
