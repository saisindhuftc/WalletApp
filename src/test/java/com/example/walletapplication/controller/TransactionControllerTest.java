package com.example.walletapplication.controller;

import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.UserAndWalletMismatchException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.exception.WalletNotFoundException;
import com.example.walletapplication.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    void testTransactionSuccess() throws Exception {
        Long userId = 1L;
        Long walletId = 1L;
        String sortBy = "timestamp";
        String sortOrder = "ASC";
        String transactionType = "DEPOSIT";

        when(transactionService.getTransactions(eq(userId), eq(walletId), eq(sortBy), eq(sortOrder), eq(transactionType)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users/{userId}/wallets/{walletId}/transactions?type={type}&sortBy={sortBy}&sortOrder={sortOrder}",
                        userId, walletId, transactionType, sortBy, sortOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    void getTransactionsBySortByAscUsingTimestamp() throws Exception {
        Long userId = 1L;
        Long walletId = 1L;
        String sortBy = "timestamp";
        String sortOrder = "ASC";
        String transactionType = "DEPOSIT";

        mockMvc.perform(get("/users/{userId}/wallets/{walletId}/transactions?type={type}&sortBy={sortBy}&sortOrder={sortOrder}",
                        userId, walletId, transactionType, sortBy, sortOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    void getTransactionsBySortByDescUsingTimestamp() throws Exception {
        Long userId = 1L;
        Long walletId = 1L;
        String sortBy = "timestamp";
        String sortOrder = "DESC";
        String transactionType = "WITHDRAWAL";

        mockMvc.perform(get("/users/{userId}/wallets/{walletId}/transactions?type={type}&sortBy={sortBy}&sortOrder={sortOrder}",
                        userId, walletId, transactionType, sortBy, sortOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    void getTransactionsBySortByAscUsingAmount() throws Exception {
        Long userId = 1L;
        Long walletId = 1L;
        String sortBy = "amount";
        String sortOrder = "ASC";
        String transactionType = "TRANSFER";

        mockMvc.perform(get("/users/{userId}/wallets/{walletId}/transactions?type={type}&sortBy={sortBy}&sortOrder={sortOrder}",
                        userId, walletId, transactionType, sortBy, sortOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    void getTransactionsBySortByDescUsingAmountForMultipleTypes() throws Exception {
        Long userId = 1L;
        Long walletId = 1L;
        String sortBy = "amount";
        String sortOrder = "DESC";
        String transactionTypes = "DEPOSIT,WITHDRAWAL";

        mockMvc.perform(get("/users/{userId}/wallets/{walletId}/transactions?types={types}&sortBy={sortBy}&sortOrder={sortOrder}",
                        userId, walletId, transactionTypes, sortBy, sortOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getAllTransactionsBySortByAscUsingTimestamp() throws Exception {
        Long userId = 1L;
        Long walletId = 1L;
        String sortBy = "timestamp";
        String sortOrder = "ASC";

        mockMvc.perform(get("/users/{userId}/wallets/{walletId}/transactions?sortBy={sortBy}&sortOrder={sortOrder}",
                        userId, walletId, sortBy, sortOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(transactionService).getTransactions(eq(userId), eq(walletId), eq(sortBy), eq(sortOrder), isNull());
    }

    @Test
    @WithMockUser
    void getAllTransactionsBySortByDescUsingAmountForMultipleTypes() throws Exception {
        Long userId = 1L;
        Long walletId = 1L;
        String sortBy = "amount";
        String sortOrder = "DESC";
        String transactionTypes = "DEPOSIT,WITHDRAWAL,TRANSFER";

        mockMvc.perform(get("/users/{userId}/wallets/{walletId}/transactions?types={types}&sortBy={sortBy}&sortOrder={sortOrder}",
                        userId, walletId, transactionTypes, sortBy, sortOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    void getTransactionsByTypeWithDefaultSorting() throws Exception {
        Long userId = 1L;
        Long walletId = 1L;
        String transactionType = "WITHDRAWAL";

        mockMvc.perform(get("/users/{userId}/wallets/{walletId}/transactions?type={type}",
                        userId, walletId, transactionType)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    void getAllTransactionsWithoutFiltersOrSorting() throws Exception {
        Long userId = 1L;
        Long walletId = 1L;

        mockMvc.perform(get("/users/{userId}/wallets/{walletId}/transactions", userId, walletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(transactionService).getTransactions(eq(userId), eq(walletId), isNull(), isNull(), isNull());
    }

    @Test
    @WithMockUser
    void getTransactionsUserNotFound() throws Exception {
        Long userId = 1L;
        Long walletId = 1L;
        String sortBy = "timestamp";
        String sortOrder = "ASC";
        String transactionType = "WITHDRAW";

        when(transactionService.getTransactions(eq(userId), eq(walletId), eq(sortBy), eq(sortOrder), eq(transactionType)))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/users/{userId}/wallets/{walletId}/transactions?sortBy={sortBy}&sortOrder={sortOrder}&transactionType={transactionType}",
                        userId, walletId, sortBy, sortOrder, transactionType)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andExpect(result -> assertEquals("User not found", result.getResolvedException().getMessage()));

        verify(transactionService).getTransactions(eq(userId), eq(walletId), eq(sortBy), eq(sortOrder), eq(transactionType));
    }

    @Test
    @WithMockUser
    void getTransactionsWalletNotFound() throws Exception {
        Long userId = 1L;
        Long walletId = 1L;
        String sortBy = "timestamp";
        String sortOrder = "ASC";
        String transactionType = "WITHDRAW";

        when(transactionService.getTransactions(eq(userId), eq(walletId), eq(sortBy), eq(sortOrder), eq(transactionType)))
                .thenThrow(new WalletNotFoundException("Wallet not found"));

        mockMvc.perform(get("/users/{userId}/wallets/{walletId}/transactions?sortBy={sortBy}&sortOrder={sortOrder}&transactionType={transactionType}",
                        userId, walletId, sortBy, sortOrder, transactionType)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof WalletNotFoundException))
                .andExpect(result -> assertEquals("Wallet not found", result.getResolvedException().getMessage()));

        verify(transactionService).getTransactions(eq(userId), eq(walletId), eq(sortBy), eq(sortOrder), eq(transactionType));
    }

    @Test
    @WithMockUser
    void getTransactionsUserAndWalletMismatch() throws Exception {
        Long userId = 1L;
        Long walletId = 1L;
        String sortBy = "timestamp";
        String sortOrder = "ASC";
        String transactionType = "WITHDRAW";

        when(transactionService.getTransactions(eq(userId), eq(walletId), eq(sortBy), eq(sortOrder), eq(transactionType)))
                .thenThrow(new UserAndWalletMismatchException("User and wallet mismatch"));

        mockMvc.perform(get("/users/{userId}/wallets/{walletId}/transactions?sortBy={sortBy}&sortOrder={sortOrder}&transactionType={transactionType}",
                        userId, walletId, sortBy, sortOrder, transactionType)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserAndWalletMismatchException))
                .andExpect(result -> assertEquals("User and wallet mismatch", result.getResolvedException().getMessage()));

        verify(transactionService).getTransactions(eq(userId), eq(walletId), eq(sortBy), eq(sortOrder), eq(transactionType));
    }

    @Test
    @WithMockUser
    void getTransactionsInsufficientBalance() throws Exception {
        Long userId = 1L;
        Long walletId = 1L;
        String sortBy = "timestamp";
        String sortOrder = "ASC";
        String transactionType = "WITHDRAW";

        when(transactionService.getTransactions(eq(userId), eq(walletId), eq(sortBy), eq(sortOrder), eq(transactionType)))
                .thenThrow(new InsufficientBalanceException("Insufficient balance"));

        mockMvc.perform(get("/users/{userId}/wallets/{walletId}/transactions?sortBy={sortBy}&sortOrder={sortOrder}&transactionType={transactionType}",
                        userId, walletId, sortBy, sortOrder, transactionType)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InsufficientBalanceException))
                .andExpect(result -> assertEquals("Insufficient balance", result.getResolvedException().getMessage()));

        verify(transactionService).getTransactions(eq(userId), eq(walletId), eq(sortBy), eq(sortOrder), eq(transactionType));
    }

    @Test
    @WithMockUser
    void getTransactionsInvalidSortField() throws Exception {
        Long userId = 1L;
        Long walletId = 1L;
        String sortBy = "invalidField";
        String sortOrder = "ASC";
        String transactionType = "WITHDRAW";

        when(transactionService.getTransactions(eq(userId), eq(walletId), eq(sortBy), eq(sortOrder), eq(transactionType)))
                .thenThrow(new IllegalArgumentException("Invalid sort field: invalidField"));

        mockMvc.perform(get("/users/{userId}/wallets/{walletId}/transactions?sortBy={sortBy}&sortOrder={sortOrder}&transactionType={transactionType}",
                        userId, walletId, sortBy, sortOrder, transactionType)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("Invalid sort field: invalidField", result.getResolvedException().getMessage()));

        verify(transactionService).getTransactions(eq(userId), eq(walletId), eq(sortBy), eq(sortOrder), eq(transactionType));
    }

    @Test
    @WithMockUser
    void getTransactionsTransactionTypeNull() throws Exception {
        Long userId = 1L;
        Long walletId = 1L;
        String sortBy = "timestamp";
        String sortOrder = "ASC";

        when(transactionService.getTransactions(eq(userId), eq(walletId), eq(sortBy), eq(sortOrder), isNull()))
                .thenThrow(new IllegalArgumentException("Transaction type cannot be null"));

        mockMvc.perform(get("/users/{userId}/wallets/{walletId}/transactions?sortBy={sortBy}&sortOrder={sortOrder}",
                        userId, walletId, sortBy, sortOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("Transaction type cannot be null", result.getResolvedException().getMessage()));

        verify(transactionService).getTransactions(eq(userId), eq(walletId), eq(sortBy), eq(sortOrder), isNull());
    }
}