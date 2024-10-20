package com.example.walletapplication.service;

import com.example.walletapplication.repository.InterWalletTransactionRepository;
import com.example.walletapplication.repository.IntraWalletTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;


class TransactionServiceTest {

    @Mock
    private IntraWalletTransactionRepository intraWalletTransactionRepository;

    @Mock
    private InterWalletTransactionRepository interWalletTransactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTransactionsUserAndWalletMismatch() {

        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.getTransactions(1L, 99L, "timestamp", "ASC", "DEPOSIT");
        });
    }

    @Test
    void getTransactionsInsufficientBalance() {
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.getTransactions(1L, 1L, "timestamp", "ASC", "WITHDRAW");
        });
    }

    @Test
    void getTransactionsInvalidSortField() {
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.getTransactions(1L, 1L, "invalidField", "ASC", "DEPOSIT");
        });
    }

    @Test
    void getTransactionsTransactionTypeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.getTransactions(1L, 1L, "timestamp", "ASC", null);
        });
    }
}
