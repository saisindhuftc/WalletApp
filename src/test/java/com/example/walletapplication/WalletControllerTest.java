package com.example.walletapplication;

import com.example.walletapplication.Controller.WalletController;
import com.example.walletapplication.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
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
        mockMvc.perform(post("/api/wallets/deposit")
                        .param("username", "Sai")
                        .param("amount", "100.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Deposit successful"));
    }

    @Test
    public void testDepositUserNotFound() throws Exception {
        doThrow(new IllegalArgumentException("User not found")).when(walletService).deposit("sravani", 100.0);

        mockMvc.perform(post("/api/wallets/deposit")
                        .param("username", "sravani")
                        .param("amount", "100.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

    @Test
    public void testWithdrawSuccess() throws Exception {
        mockMvc.perform(post("/api/wallets/withdraw")
                        .param("username", "Lahari")
                        .param("amount", "1000.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Withdrawal successful"));
    }

    @Test
    public void testWithdrawUserNotFound() throws Exception {
        doThrow(new IllegalArgumentException("User not found")).when(walletService).withdraw("nonexistent", 50.0);

        mockMvc.perform(post("/api/wallets/withdraw")
                        .param("username", "nonexistent")
                        .param("amount", "50.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found"));
    }

    @Test
    public void testWithdrawInsufficientBalance() throws Exception {
        doThrow(new IllegalArgumentException("Insufficient balance")).when(walletService).withdraw("Sai", 2000.0);

        mockMvc.perform(post("/api/wallets/withdraw")
                        .param("username", "Sai")
                        .param("amount", "2000.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient balance"));
    }


}
