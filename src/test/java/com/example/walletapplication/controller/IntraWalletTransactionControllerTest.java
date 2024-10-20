package com.example.walletapplication.controller;

import com.example.walletapplication.Security.SecurityConfigTest;
import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.enums.TransactionType;
import com.example.walletapplication.exception.*;
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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
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
    public void testDepositSuccess() throws Exception {
        doNothing().when(walletService).deposit(any(Long.class), any(Double.class), any(CurrencyType.class));
        doNothing().when(walletService).isUserAuthorized(any(Long.class), any(Long.class));

        mockMvc.perform(post("/users/1/wallets/1/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Deposit successful"))
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andDo(print());

        verify(walletService, Mockito.times(1)).isUserAuthorized(1L, 1L);
    }

    @Test
    @WithMockUser
    public void testDepositFailedException() throws Exception {
        doThrow(new DepositFailedException("Deposit failed")).when(walletService).deposit(any(Long.class), any(Double.class), any(CurrencyType.class));

        mockMvc.perform(post("/users/1/wallets/1/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("Deposit failed"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void testDepositWhenUserIsUnAuthorizedException() throws Exception {
        doThrow(new UnAuthorisedUserException("User not authorized")).when(walletService).isUserAuthorized(any(Long.class), any(Long.class));

        mockMvc.perform(post("/users/1/wallets/1/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value("User not authorized"))
                .andDo(print());
        verify(walletService, never()).deposit(any(Long.class), any(Double.class), any(CurrencyType.class));
    }

    @Test
    @WithMockUser
    public void testDepositWhenUnAuthorisedWalletException() throws Exception {
        doThrow(new UnAuthorisedWalletException("User not authorized for this wallet")).when(walletService).isUserAuthorized(any(Long.class), any(Long.class));

        mockMvc.perform(post("/users/1/wallets/1/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value("User not authorized for this wallet"))
                .andDo(print());
        verify(walletService, never()).deposit(any(Long.class), any(Double.class), any(CurrencyType.class));
    }

    @Test
    @WithMockUser
    public void testDepositWhenUserNotFoundException() throws Exception {
        doThrow(new UserNotFoundException("User not found")).when(walletService).isUserAuthorized(any(Long.class), any(Long.class));

        mockMvc.perform(post("/users/1/wallets/1/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("User not found"))
                .andDo(print());

        verify(walletService, never()).deposit(any(Long.class), any(Double.class), any(CurrencyType.class));
    }

    @Test
    @WithMockUser
    public void testDepositInvalidCurrencyException() throws Exception {
        doThrow(new InvalidCurrencyException("Invalid currency")).when(walletService).deposit(any(Long.class), any(Double.class), any(CurrencyType.class));

        mockMvc.perform(post("/users/1/wallets/1/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("Invalid request"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void testDepositInvalidAmountException() throws Exception {
        doThrow(new InvalidAmountException("Invalid amount")).when(walletService).deposit(any(Long.class), any(Double.class), any(CurrencyType.class));

        mockMvc.perform(post("/users/1/wallets/1/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("Invalid amount"))
                .andDo(print());

    }

    @Test
    @WithMockUser
    public void testWithdrawSuccess() throws Exception {
        doNothing().when(walletService).isUserAuthorized(any(Long.class), any(Long.class));
        Mockito.when(walletService.withdraw(any(Long.class), eq(50.0), eq(CurrencyType.USD)))
                .thenReturn(50.0);

        mockMvc.perform(post("/users/1/wallets/1/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Withdrawal successful"))
                .andExpect(jsonPath("$.withdrawnAmount").value(50.0))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andDo(print());

        verify(walletService, Mockito.times(1)).withdraw(1L, 50.0, CurrencyType.USD);
    }

    @Test
    @WithMockUser
    public void testWithdrawWhenUserIsUnAuthorizedException() throws Exception {
        doThrow(new UnAuthorisedUserException("User not authorized")).when(walletService).isUserAuthorized(any(Long.class), any(Long.class));

        mockMvc.perform(post("/users/1/wallets/1/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value("User not authorized"))
                .andDo(print());

        verify(walletService, never()).withdraw(any(Long.class), any(Double.class), any(CurrencyType.class));
    }

    @Test
    @WithMockUser
    public void testWithdrawWhenUnAuthorisedWalletException() throws Exception {
        doThrow(new UnAuthorisedWalletException("User not authorized for this wallet")).when(walletService).isUserAuthorized(any(Long.class), any(Long.class));

        mockMvc.perform(post("/users/1/wallets/1/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value("User not authorized for this wallet"))
                .andDo(print());

        verify(walletService, never()).withdraw(any(Long.class), any(Double.class), any(CurrencyType.class));
    }

    @Test
    @WithMockUser
    public void testWithdrawInvalidCurrencyException() throws Exception {
        doThrow(new InvalidCurrencyException("Invalid currency")).when(walletService).withdraw(any(Long.class), any(Double.class), any(CurrencyType.class));

        mockMvc.perform(post("/users/1/wallets/1/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("Invalid request"))
                .andExpect(jsonPath("$.message").value("Invalid currency"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void testWithdrawInvalidAmountException() throws Exception {
        doThrow(new InvalidAmountException("Invalid amount")).when(walletService).withdraw(any(Long.class), any(Double.class), any(CurrencyType.class));

        mockMvc.perform(post("/users/1/wallets/1/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("Invalid amount"))
                .andDo(print());
    }


    @Test
    @WithMockUser
    public void testWithdrawFailedException() throws Exception {
        doThrow(new WithdrawalFailedException("Withdrawal failed")).when(walletService).withdraw(any(Long.class), any(Double.class), any(CurrencyType.class));

        mockMvc.perform(post("/users/1/wallets/1/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("Withdrawal failed"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void testWithdrawUserNotFoundException() throws Exception {
        doThrow(new UserNotFoundException("User not found")).when(walletService).isUserAuthorized(any(Long.class), any(Long.class));

        mockMvc.perform(post("/users/1/wallets/1/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("User not found"))
                .andDo(print());

        verify(walletService, never()).withdraw(any(Long.class), any(Double.class), any(CurrencyType.class));
    }

    @Test
    @WithMockUser
    public void testWithdrawInsufficientBalanceException() throws Exception {
        doThrow(new InsufficientBalanceException("Insufficient balance")).when(walletService).withdraw(any(Long.class), any(Double.class), any(CurrencyType.class));

        mockMvc.perform(post("/users/1/wallets/1/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("Insufficient balance"))
                .andDo(print());
    }
    @Test
    @WithMockUser
    public void testGetDepositsSuccess() throws Exception {
        doNothing().when(walletService).isUserAuthorized(any(Long.class), any(Long.class));

        mockMvc.perform(get("/users/1/wallets/1/deposits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(walletService, Mockito.times(1)).getDeposits(1L);
    }

    @Test
    @WithMockUser
    public void testGetDepositsWalletNotFound() throws Exception {
        doNothing().when(walletService).isUserAuthorized(any(Long.class), any(Long.class));
        doThrow(new WalletNotFoundException("Wallet not found")).when(walletService).getDeposits(any(Long.class));

        mockMvc.perform(get("/users/1/wallets/999/deposits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Wallet not found"))
                .andDo(print());

        verify(walletService, Mockito.times(1)).isUserAuthorized(1L, 999L);
    }

    @Test
    @WithMockUser
    public void testGetWithdrawalsWalletNotFound() throws Exception {
        doNothing().when(walletService).isUserAuthorized(any(Long.class), any(Long.class));
        doThrow(new WalletNotFoundException("Wallet not found")).when(walletService).getWithdrawals(any(Long.class));

        mockMvc.perform(get("/users/1/wallets/999/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Wallet not found"))
                .andDo(print());

        verify(walletService, Mockito.times(1)).isUserAuthorized(1L, 999L);
    }


    @Test
    @WithMockUser
    public void testGetDepositsInternalError() throws Exception {
        doNothing().when(walletService).isUserAuthorized(any(Long.class), any(Long.class));
        doThrow(new RuntimeException("Unexpected error")).when(walletService).getDeposits(any(Long.class));

        mockMvc.perform(get("/users/1/wallets/1/deposits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Unexpected error"))
                .andDo(print());

        verify(walletService, Mockito.times(1)).isUserAuthorized(1L, 1L);
    }

    @Test
    @WithMockUser
    public void testGetWithdrawalsInternalError() throws Exception {
        doNothing().when(walletService).isUserAuthorized(any(Long.class), any(Long.class));
        doThrow(new RuntimeException("Unexpected error")).when(walletService).getWithdrawals(any(Long.class));

        mockMvc.perform(get("/users/1/wallets/1/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Unexpected error"))
                .andDo(print());

        verify(walletService, Mockito.times(1)).isUserAuthorized(1L, 1L);
    }


    @Test
    @WithMockUser
    public void testGetDepositsEmpty() throws Exception {
        doNothing().when(walletService).isUserAuthorized(any(Long.class), any(Long.class));
        when(walletService.getDeposits(any(Long.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users/1/wallets/1/deposits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());

        verify(walletService, Mockito.times(1)).getDeposits(1L);
    }

    @Test
    @WithMockUser
    public void testGetWithdrawalsEmpty() throws Exception {
        doNothing().when(walletService).isUserAuthorized(any(Long.class), any(Long.class));
        when(walletService.getWithdrawals(any(Long.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users/1/wallets/1/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());

        verify(walletService, Mockito.times(1)).getWithdrawals(1L);
    }


    @Test
    @WithMockUser
    public void testGetDepositsUserAndWalletMismatch() throws Exception {
        doThrow(new UnAuthorisedUserException("User not authorized")).when(walletService).isUserAuthorized(any(Long.class), any(Long.class));

        mockMvc.perform(get("/users/2/wallets/1/deposits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("User not authorized"))
                .andDo(print());

        verify(walletService, Mockito.times(1)).isUserAuthorized(2L, 1L);
    }

    @Test
    @WithMockUser
    public void testGetWithdrawalsUserAndWalletMismatch() throws Exception {
        doThrow(new UnAuthorisedUserException("User not authorized")).when(walletService).isUserAuthorized(any(Long.class), any(Long.class));

        mockMvc.perform(get("/users/2/wallets/1/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("User not authorized"))
                .andDo(print());

        verify(walletService, Mockito.times(1)).isUserAuthorized(2L, 1L);
    }

    @Test
    @WithMockUser
    public void testGetWithdrawalsSuccess() throws Exception {
        doNothing().when(walletService).isUserAuthorized(any(Long.class), any(Long.class));

        mockMvc.perform(get("/users/1/wallets/1/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(walletService, Mockito.times(1)).getWithdrawals(1L);
    }

}