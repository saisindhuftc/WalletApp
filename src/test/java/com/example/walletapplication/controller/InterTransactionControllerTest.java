package com.example.walletapplication.controller;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.exception.UnAuthorisedUserException;
import com.example.walletapplication.exception.UnAuthorisedWalletException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.requestDTO.InterTransactionRequestDTO;
import com.example.walletapplication.service.InterTransactionService;
import com.example.walletapplication.service.IntraTransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InterTransactionController.class)
class InterTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private InterTransactionService transactionService;

    @Mock
    private IntraTransactionService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    public void testTransferSuccess() throws Exception {
        InterTransactionRequestDTO requestDTO = new InterTransactionRequestDTO();
        requestDTO.setAmount(100.0);
        requestDTO.setCurrency(CurrencyType.USD);
        String jsonRequestBody = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/wallets/2/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successful. Amount: 100.0"));

        verify(transactionService, times(1)).transfer(1L, 2L, 100.0, CurrencyType.USD);
        verify(walletService, times(1)).isUserAuthorized(1L, 2L);
    }


    @Test
    @WithMockUser
    public void testTransferUnAuthorizedUser() throws Exception {
        InterTransactionRequestDTO requestDTO = new InterTransactionRequestDTO();
        requestDTO.setAmount(100.0);
        requestDTO.setSenderWalletId(1L);
        requestDTO.setReceiverWalletId(2L);
        requestDTO.setCurrency(CurrencyType.USD);
        String jsonRequestBody = objectMapper.writeValueAsString(requestDTO);

        doThrow(new UnAuthorisedUserException("User not authorized")).when(walletService).isUserAuthorized(2L, 1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/{userId}/wallets/{walletId}/transfer", 2L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isForbidden())
                .andExpect(content().string("User not authorized"));

        verify(walletService, times(1)).isUserAuthorized(2L, 1L);
        verifyNoInteractions(transactionService);
    }

    @Test
    @WithMockUser
    public void testTransferUnAuthorizedWallet() throws Exception {
        InterTransactionRequestDTO requestDTO = new InterTransactionRequestDTO();
        requestDTO.setAmount(100.0);
        requestDTO.setSenderWalletId(1L);
        requestDTO.setReceiverWalletId(77L);
        requestDTO.setCurrency(CurrencyType.USD);
        String jsonRequestBody = objectMapper.writeValueAsString(requestDTO);

        doThrow(new UnAuthorisedWalletException("User not authorized for this Wallet")).when(walletService).isUserAuthorized(1L, 77L);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/{userId}/wallets/{walletId}/transfer", 1L, 77L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestBody))
                .andExpect(status().isForbidden())
                .andExpect(content().string("User not authorized for this Wallet"));

        verify(walletService, times(1)).isUserAuthorized(1L, 77L);
        verifyNoInteractions(transactionService);
    }

    @Test
    @WithMockUser
    public void testGetTransactionHistorySuccess() throws Exception {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        List<Object> mockTransactions = Collections.singletonList("Sample Transaction");
        when(transactionService.getTransactionHistory(3L, startDate, endDate)).thenReturn(mockTransactions);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/wallets/{walletId}/transactions", 2L, 3L)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"Sample Transaction\"]"));

        verify(walletService, times(1)).isUserAuthorized(2L, 3L);
        verify(transactionService, times(1)).getTransactionHistory(3L, startDate, endDate);
    }

    @Test
    @WithMockUser
    public void testGetTransactionHistoryUserNotFound() throws Exception {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        doThrow(new UserNotFoundException("User not found")).when(walletService).isUserAuthorized(3L, 5L);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/wallets/{walletId}/transactions", 3L, 5L)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));

        verify(walletService, times(1)).isUserAuthorized(3L, 5L);
        verifyNoInteractions(transactionService);
    }
}
