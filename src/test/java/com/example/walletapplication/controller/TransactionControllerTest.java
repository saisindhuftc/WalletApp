package com.example.walletapplication.controller;

import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.exception.UnAuthorisedUserException;
import com.example.walletapplication.exception.UnAuthorisedWalletException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.requestDTO.InterTransactionRequestDTO;
import com.example.walletapplication.service.TransactionService;
import com.example.walletapplication.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTransferSuccess() {
        InterTransactionRequestDTO requestDTO = new InterTransactionRequestDTO();
        requestDTO.setAmount(100.0);
        requestDTO.setSenderWalletId(1L);
        requestDTO.setReceiverWalletId(2L);
        requestDTO.setCurrency(CurrencyType.USD);

        ResponseEntity<?> response = transactionController.transfer(1L, 1L, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transfer successful. Amount: 100.0", response.getBody());
        verify(transactionService, times(1)).transfer(1L, 2L, 100.0, CurrencyType.USD);
        verify(walletService, times(1)).isUserAuthorized(1L, 1L);
    }

    @Test
    public void TesttransferUnAuthorizedUser() {
        InterTransactionRequestDTO requestDTO = new InterTransactionRequestDTO();
        doThrow(new UnAuthorisedUserException("User not authorized")).when(walletService).isUserAuthorized(2L, 2L);

        ResponseEntity<?> response = transactionController.transfer(2L,2L, requestDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("User not authorized", response.getBody());
        verify(walletService, times(1)).isUserAuthorized(2L,2L);
        verifyNoInteractions(transactionService);
    }

    @Test
    public void TestTransferUnAuthorizedWallet() {
        InterTransactionRequestDTO requestDTO = new InterTransactionRequestDTO();
        doThrow(new UnAuthorisedWalletException("User not authorized for this Wallet")).when(walletService).isUserAuthorized(1L,77L);

        ResponseEntity<?> response = transactionController.transfer(1L, 77L, requestDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("User not authorized for this Wallet", response.getBody());
        verify(walletService, times(1)).isUserAuthorized(1L, 77L);
        verifyNoInteractions(transactionService);
    }


    @Test
    public void getTransactionHistorySuccess() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        List<Object> mockTransactions = Collections.singletonList("Sample Transaction");
        when(transactionService.getTransactionHistory(3L, startDate, endDate)).thenReturn(mockTransactions);

        ResponseEntity<?> response = transactionController.getTransactionHistory(2L,3L, startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTransactions, response.getBody());
        verify(walletService, times(1)).isUserAuthorized(2L,3L);
        verify(transactionService, times(1)).getTransactionHistory(3L, startDate, endDate);
    }

    @Test
    public void getTransactionHistoryUserNotFound() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        doThrow(new UserNotFoundException("User not found")).when(walletService).isUserAuthorized(3L,5L);

        ResponseEntity<?> response = transactionController.getTransactionHistory(3L,5L, startDate, endDate);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(walletService, times(1)).isUserAuthorized(3L,5L);
        verifyNoInteractions(transactionService);
    }
}
