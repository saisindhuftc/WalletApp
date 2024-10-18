package com.example.walletapplication.controller;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.exception.*;
import com.example.walletapplication.requestDTO.WalletRequestDTO;
import com.example.walletapplication.service.IntraTransactionService;
import com.example.walletapplication.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IntraTransactionController.class)
public class IntraTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private IntraTransactionService walletService;

    @Mock
    private UserService userService;

    @InjectMocks
    private IntraTransactionController intraTransactionController;

    @Autowired
    private ObjectMapper objectMapper;

    private Long userId = 1L;
    private Long walletId = 1L;

    @BeforeEach
    public void setup() {
        // Reset mocks before each test
        reset(walletService);
    }

    // Test case for null amount during deposit
    @Test
    public void testDepositWhenAmountIsNull() throws Exception {
        WalletRequestDTO requestBody = new WalletRequestDTO(null, CurrencyType.USD); // Amount is null
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/{userId}/wallets/{walletId}/deposits", userId, walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Amount cannot be null"));

        verify(walletService, times(0)).deposit(userId, null, CurrencyType.USD);
    }

    // Test case for successful deposit
    @Test
    public void testDepositSuccess() throws Exception {
        WalletRequestDTO requestBody = new WalletRequestDTO(100.0, CurrencyType.USD); // Valid amount
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/{userId}/wallets/{walletId}/deposits", userId, walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("100.0"));

        verify(walletService, times(1)).deposit(userId, 100.0, CurrencyType.USD);
    }

    // Test case when the user is not authorized
    @Test
    public void testDepositWhenUserUnauthorized() throws Exception {
        WalletRequestDTO requestBody = new WalletRequestDTO(100.0, CurrencyType.USD);
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        doThrow(new UnAuthorisedUserException("User not authorized")).when(walletService).isUserAuthorized(userId, walletId);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/{userId}/wallets/{walletId}/deposits", userId, walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isForbidden())
                .andExpect(content().string("User not authorized"));

        verify(walletService, times(0)).deposit(userId, 100.0, CurrencyType.USD);
    }

    // Test case when the wallet is not authorized
    @Test
    public void testDepositWhenWalletUnauthorized() throws Exception {
        WalletRequestDTO requestBody = new WalletRequestDTO(100.0, CurrencyType.USD);
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        doThrow(new UnAuthorisedWalletException("Wallet not authorized")).when(walletService).isUserAuthorized(userId, walletId);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/{userId}/wallets/{walletId}/deposits", userId, walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Wallet not authorized"));

        verify(walletService, times(0)).deposit(userId, 100.0, CurrencyType.USD);
    }

    // Test case when the user is not found
    @Test
    public void testDepositWhenUserNotFound() throws Exception {
        WalletRequestDTO requestBody = new WalletRequestDTO(100.0, CurrencyType.USD);
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        doThrow(new UserNotFoundException("User not found")).when(walletService).isUserAuthorized(userId, walletId);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/{userId}/wallets/{walletId}/deposits", userId, walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(walletService, times(0)).deposit(userId, 100.0, CurrencyType.USD);
    }

    // Test case when an internal server error occurs
    @Test
    public void testDepositWhenInternalServerError() throws Exception {
        WalletRequestDTO requestBody = new WalletRequestDTO(100.0, CurrencyType.USD);
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        doThrow(new RuntimeException("Some internal error")).when(walletService).deposit(userId, 100.0, CurrencyType.USD);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/{userId}/wallets/{walletId}/deposits", userId, walletId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred: Some internal error"));

        verify(walletService, times(1)).deposit(userId, 100.0, CurrencyType.USD);
    }

    // Test case: Withdraw when amount is null
    @Test
    public void testWithdrawWhenAmountIsNull() throws Exception {
        WalletRequestDTO requestBody = new WalletRequestDTO(null, CurrencyType.USD);
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/wallets/1/withdrawls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Amount cannot be null"));

        verify(walletService, times(0)).withdraw(anyLong(), anyDouble(), any(CurrencyType.class));
    }

    // Test case: Withdraw when unauthorized
    @Test
    public void testWithdrawWhenUserIsUnauthorized() throws Exception {
        WalletRequestDTO requestBody = new WalletRequestDTO(100.0, CurrencyType.USD);
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        doThrow(new UnAuthorisedUserException("Unauthorized user"))
                .when(walletService).isUserAuthorized(1L, 1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/wallets/1/withdrawls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Unauthorized user"));

        verify(walletService, times(1)).isUserAuthorized(1L, 1L);
        verify(walletService, times(0)).withdraw(anyLong(), anyDouble(), any(CurrencyType.class));
    }

    // Test case: Successful withdraw
    @Test
    public void testWithdrawSuccess() throws Exception {
        WalletRequestDTO requestBody = new WalletRequestDTO(50.0, CurrencyType.USD);
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        when(walletService.withdraw(1L, 50.0, CurrencyType.USD)).thenReturn(50.0);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/wallets/1/withdrawls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("50.0"));

        verify(walletService, times(1)).isUserAuthorized(1L, 1L);
        verify(walletService, times(1)).withdraw(1L, 50.0, CurrencyType.USD);
    }

    // Test case: Withdraw when user not found
    @Test
    public void testWithdrawWhenUserNotFound() throws Exception {
        WalletRequestDTO requestBody = new WalletRequestDTO(50.0, CurrencyType.USD);
        String jsonRequestBody = objectMapper.writeValueAsString(requestBody);

        doThrow(new UserNotFoundException("User not found"))
                .when(walletService).isUserAuthorized(1L, 1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/wallets/1/withdrawls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(walletService, times(1)).isUserAuthorized(1L, 1L);
        verify(walletService, times(0)).withdraw(anyLong(), anyDouble(), any(CurrencyType.class));
    }
}

