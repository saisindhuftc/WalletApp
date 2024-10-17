package com.example.walletapplication.controller;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.UnAuthorisedUserException;
import com.example.walletapplication.exception.UnAuthorisedWalletException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.requestDTO.WalletRequestDTO;
import com.example.walletapplication.service.UserService;
import com.example.walletapplication.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WalletControllerTest {
    @InjectMocks
    private WalletController walletController;

    @Mock
    private UserService userService;

    @Mock
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDepositSuccess() {
        Long userId = 1L;
        Long walletId = 2L;
        WalletRequestDTO depositRequestDTO = new WalletRequestDTO(100.0, CurrencyType.USD);

        doNothing().when(walletService).isUserAuthorized(userId, walletId);
        doNothing().when(walletService).deposit(userId, 100.0, CurrencyType.USD); // No return value expected

        ResponseEntity<?> response = walletController.deposit(userId, walletId, depositRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(100.0, response.getBody()); // This should be adjusted as per your actual response structure
        verify(walletService, times(1)).isUserAuthorized(userId, walletId);
        verify(walletService, times(1)).deposit(userId, 100.0, CurrencyType.USD);
    }

    @Test
    public void testWithdrawSuccess() {
        Long userId = 1L;
        Long walletId = 2L;
        WalletRequestDTO withdrawRequestDTO = new WalletRequestDTO(50.0, CurrencyType.USD);

        doNothing().when(walletService).isUserAuthorized(userId, walletId);
        when(walletService.withdraw(userId, 50.0, CurrencyType.USD)).thenReturn(50.0);

        ResponseEntity<?> response = walletController.withdraw(userId, walletId, withdrawRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(50.0, response.getBody());
        verify(walletService, times(1)).isUserAuthorized(userId, walletId);
        verify(walletService, times(1)).withdraw(userId, 50.0, CurrencyType.USD);
    }

    @Test
    public void testDepositUserNotFound() {
        Long userId = 1L;
        Long walletId = 2L;
        WalletRequestDTO depositRequestDTO = new WalletRequestDTO(100.0, CurrencyType.USD);

        doThrow(new UserNotFoundException("User not found")).when(walletService).isUserAuthorized(userId, walletId);

        ResponseEntity<?> response = walletController.deposit(userId, walletId, depositRequestDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(walletService, times(1)).isUserAuthorized(userId, walletId);
    }

    @Test
    public void testDepositUnauthorizedUser() {
        Long userId = 1L;
        Long walletId = 2L;
        WalletRequestDTO depositRequestDTO = new WalletRequestDTO(100.0, CurrencyType.USD);

        doThrow(new UnAuthorisedUserException("User not authorized")).when(walletService).isUserAuthorized(userId, walletId);

        ResponseEntity<?> response = walletController.deposit(userId, walletId, depositRequestDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("User not authorized", response.getBody());
        verify(walletService, times(1)).isUserAuthorized(userId, walletId);
    }

    @Test
    public void testWithdrawUserNotFound() {
        Long userId = 1L;
        Long walletId = 2L;
        WalletRequestDTO withdrawRequestDTO = new WalletRequestDTO(50.0, CurrencyType.USD);

        doThrow(new UserNotFoundException("User not found")).when(walletService).isUserAuthorized(userId, walletId);

        ResponseEntity<?> response = walletController.withdraw(userId, walletId, withdrawRequestDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(walletService, times(1)).isUserAuthorized(userId, walletId);
    }

    @Test
    public void testWithdrawUnauthorizedUser() {
        Long userId = 1L;
        Long walletId = 2L;
        WalletRequestDTO withdrawRequestDTO = new WalletRequestDTO(50.0, CurrencyType.USD);

        doThrow(new UnAuthorisedUserException("User not authorized")).when(walletService).isUserAuthorized(userId, walletId);

        ResponseEntity<?> response = walletController.withdraw(userId, walletId, withdrawRequestDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("User not authorized", response.getBody());
        verify(walletService, times(1)).isUserAuthorized(userId, walletId);
    }

    @Test
    public void testWithdrawUnauthorizedWallet() {
        Long userId = 1L;
        Long walletId = 2L;
        WalletRequestDTO withdrawRequestDTO = new WalletRequestDTO(50.0, CurrencyType.USD);

        doThrow(new UnAuthorisedWalletException("User not authorized for this Wallet")).when(walletService).isUserAuthorized(userId, walletId);

        ResponseEntity<?> response = walletController.withdraw(userId, walletId, withdrawRequestDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("User not authorized for this Wallet", response.getBody());
        verify(walletService, times(1)).isUserAuthorized(userId, walletId);
    }

    @Test
    public void testWithdrawInsufficientBalance() {
        Long userId = 1L;
        Long walletId = 2L;
        WalletRequestDTO withdrawRequestDTO = new WalletRequestDTO(50.0, CurrencyType.USD);

        doNothing().when(walletService).isUserAuthorized(userId, walletId);
        doThrow(new InsufficientBalanceException("Insufficient balance")).when(walletService).withdraw(userId, 50.0, CurrencyType.USD);

        ResponseEntity<?> response = walletController.withdraw(userId, walletId, withdrawRequestDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred: Insufficient balance", response.getBody());
        verify(walletService, times(1)).isUserAuthorized(userId, walletId);
        verify(walletService, times(1)).withdraw(userId, 50.0, CurrencyType.USD);
    }

    @Test
    public void testDepositGeneralException() {
        Long userId = 1L;
        Long walletId = 2L;
        WalletRequestDTO depositRequestDTO = new WalletRequestDTO(100.0, CurrencyType.USD);

        doNothing().when(walletService).isUserAuthorized(userId, walletId);
        doThrow(new RuntimeException("An unexpected error")).when(walletService).deposit(userId, 100.0, CurrencyType.USD);

        ResponseEntity<?> response = walletController.deposit(userId, walletId, depositRequestDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred: An unexpected error", response.getBody());
        verify(walletService, times(1)).isUserAuthorized(userId, walletId);
        verify(walletService, times(1)).deposit(userId, 100.0, CurrencyType.USD);
    }

}
