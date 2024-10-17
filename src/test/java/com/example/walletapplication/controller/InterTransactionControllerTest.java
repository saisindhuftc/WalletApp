package com.example.walletapplication.controller;

import com.example.walletapplication.exception.SameWalletsForTransactionException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.exception.WalletNotFoundException;
import com.example.walletapplication.requestDTO.InterTransactionRequestDTO;
import com.example.walletapplication.responseModels.InterTransactionResponseModel;
import com.example.walletapplication.service.InterTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class InterTransactionControllerTest {

    @InjectMocks
    private InterTransactionController interTransactionController;

    @Mock
    private InterTransactionService interTransactionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTransactSuccess() throws Exception {
        InterTransactionRequestDTO requestModel = new InterTransactionRequestDTO();
        InterTransactionResponseModel responseModel = new InterTransactionResponseModel();

        when(interTransactionService.transact(any(InterTransactionRequestDTO.class)))
                .thenReturn(responseModel);

        ResponseEntity<?> responseEntity = interTransactionController.transfer(requestModel);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals(responseModel, responseEntity.getBody());
        verify(interTransactionService, times(1)).transact(requestModel);
    }

    @Test
    public void testTransactWalletNotFound() throws Exception {
        InterTransactionRequestDTO requestModel = new InterTransactionRequestDTO();

        when(interTransactionService.transact(any(InterTransactionRequestDTO.class)))
                .thenThrow(new WalletNotFoundException("Wallet not found"));

        ResponseEntity<?> responseEntity = interTransactionController.transfer(requestModel);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Wallet not found", responseEntity.getBody());
        verify(interTransactionService, times(1)).transact(requestModel);
    }

    @Test
    public void testTransactSameWalletsForTransaction() throws Exception {
        InterTransactionRequestDTO requestModel = new InterTransactionRequestDTO();

        when(interTransactionService.transact(any(InterTransactionRequestDTO.class)))
                .thenThrow(new SameWalletsForTransactionException("Cannot transact between the same wallets"));

        ResponseEntity<?> responseEntity = interTransactionController.transfer(requestModel);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Cannot transact between the same wallets", responseEntity.getBody());
        verify(interTransactionService, times(1)).transact(requestModel);
    }

    @Test
    public void testTransactUserNotFound() throws Exception {
        InterTransactionRequestDTO requestModel = new InterTransactionRequestDTO();

        when(interTransactionService.transact(any(InterTransactionRequestDTO.class)))
                .thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<?> responseEntity = interTransactionController.transfer(requestModel);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("User not found", responseEntity.getBody());
        verify(interTransactionService, times(1)).transact(requestModel);
    }

}