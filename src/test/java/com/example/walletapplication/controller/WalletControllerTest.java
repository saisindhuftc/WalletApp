package com.example.walletapplication.controller;

import com.example.walletapplication.entity.Transaction;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class WalletControllerTest {

    @InjectMocks
    private WalletController walletController;

    @Mock
    private WalletService walletService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    public void testDepositSuccess() throws Exception {
        mockMvc.perform(post("/api/wallets/1/deposit")
                        .param("amount", "100.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Deposit successful"));
    }

    @Test
    public void testDepositUserNotFound() throws Exception {
        doThrow(new UserNotFoundException("User not found")).when(walletService).deposit(1L, 100.0);

        mockMvc.perform(post("/api/wallets/1/deposit")
                        .param("amount", "100.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    public void testWithdrawSuccess() throws Exception {
        mockMvc.perform(post("/api/wallets/1/withdraw")
                        .param("amount", "100.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Withdrawal successful"));
    }

    @Test
    public void testWithdrawUserNotFound() throws Exception {
        doThrow(new UserNotFoundException("User not found")).when(walletService).withdraw(1L, 100.0);

        mockMvc.perform(post("/api/wallets/1/withdraw")
                        .param("amount", "100.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    public void testWithdrawInsufficientBalance() throws Exception {
        doThrow(new InsufficientBalanceException("Insufficient balance")).when(walletService).withdraw(1L, 100.0);

        mockMvc.perform(post("/api/wallets/1/withdraw")
                        .param("amount", "100.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient balance"));
    }

    @Test
    public void testTransferSuccess() throws Exception {
        mockMvc.perform(post("/api/wallets/transfer")
                        .param("fromUserId", "1")
                        .param("toUserId", "2")
                        .param("amount", "100.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successful"));
    }

    @Test
    public void testTransferUserNotFound() throws Exception {
        doThrow(new UserNotFoundException("User not found")).when(walletService).transfer(1L, 2L, 100.0);

        mockMvc.perform(post("/api/wallets/transfer")
                        .param("fromUserId", "1")
                        .param("toUserId", "2")
                        .param("amount", "100.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    public void testGetTransactionHistorySuccess() throws Exception {
        when(walletService.getTransactionHistory(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/wallets/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetTransactionHistoryUserNotFound() throws Exception {
        doThrow(new UserNotFoundException("User not found")).when(walletService).getTransactionHistory(1L);

        mockMvc.perform(get("/api/wallets/1/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}