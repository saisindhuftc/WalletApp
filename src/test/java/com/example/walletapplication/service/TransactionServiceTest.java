package com.example.walletapplication.service;

import com.example.walletapplication.repository.InterWalletTransactionRepository;
import com.example.walletapplication.repository.IntraWalletTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        Long userId = 1L;
        Long walletId = 1L;
        String sortBy = "invalidField";
        String sortOrder = "ASC";
        String transactionType = "WITHDRAW";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.getTransactions(userId, walletId, sortBy, sortOrder, transactionType);
        });

        assertEquals("Invalid sort field: invalidField", exception.getMessage());
    }

    @Test
    void getTransactionsTransactionTypeNull() {
        Long userId = 1L;
        Long walletId = 1L;
        String sortBy = "timestamp";
        String sortOrder = "ASC";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.getTransactions(userId, walletId, sortBy, sortOrder, null);
        });

        assertEquals("Transaction type cannot be null", exception.getMessage());
    }


}
