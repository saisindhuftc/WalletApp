package com.example.walletapplication.controller;

import com.example.walletapplication.Security.SecurityConfigTest;
import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.enums.TransactionType;
import com.example.walletapplication.requestDTO.IntraWalletTransactionRequestDTO;
import com.example.walletapplication.service.IntraWalletTransactionService;
import com.example.walletapplication.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(IntraWalletTransactionController.class)
@Import(SecurityConfigTest.class)
public class IntraWalletTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IntraWalletTransactionService walletService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private IntraWalletTransactionRequestDTO depositRequest;
    private IntraWalletTransactionRequestDTO withdrawRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        depositRequest = new IntraWalletTransactionRequestDTO(TransactionType.DEPOSIT, 100.0, CurrencyType.USD);
        withdrawRequest = new IntraWalletTransactionRequestDTO(TransactionType.WITHDRAWAL, 50.0, CurrencyType.USD);
    }

    @Test
    @WithMockUser
    public void testDeposit() throws Exception {
        doNothing().when(walletService).deposit(any(Long.class), any(Double.class), any(CurrencyType.class));
        doNothing().when(walletService).isUserAuthorized(any(Long.class), any(Long.class));

        mockMvc.perform(post("/users/{userId}/wallets/{walletId}/deposits", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deposit successful"))
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andDo(print());

        verify(walletService).deposit(1L, 100.0, CurrencyType.USD);
    }

    @Test
    @WithMockUser
    public void testWithdraw() throws Exception {
        doNothing().when(walletService).isUserAuthorized(any(Long.class), any(Long.class));
        Mockito.when(walletService.withdraw(any(Long.class), eq(50.0), eq(CurrencyType.USD)))
                .thenReturn(50.0);

        mockMvc.perform(post("/users/{userId}/wallets/{walletId}/withdrawals", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Withdrawal successful"))
                .andExpect(jsonPath("$.withdrawnAmount").value(50.0))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andDo(print());

        verify(walletService).withdraw(1L, 50.0, CurrencyType.USD);
    }

    @Test
    @WithMockUser
    public void testGetDeposits() throws Exception {
        doNothing().when(walletService).isUserAuthorized(any(Long.class), any(Long.class));

        mockMvc.perform(get("/users/{userId}/wallets/{walletId}/deposits", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(walletService).getDeposits(1L);
    }

    @Test
    @WithMockUser
    public void testGetWithdrawals() throws Exception {
        doNothing().when(walletService).isUserAuthorized(any(Long.class), any(Long.class));

        mockMvc.perform(get("/users/{userId}/wallets/{walletId}/withdrawals", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(walletService).getWithdrawals(1L);
    }
}