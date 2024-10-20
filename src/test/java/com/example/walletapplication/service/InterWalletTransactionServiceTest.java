package com.example.walletapplication.service;

import com.example.walletapplication.entity.InterWalletTransaction;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.entity.Wallet;
import com.example.walletapplication.enums.CurrencyType;
import com.example.walletapplication.exception.*;
import com.example.walletapplication.repository.InterWalletTransactionRepository;
import com.example.walletapplication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterWalletTransactionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InterWalletTransactionRepository interWalletTransactionRepository;

    @InjectMocks
    private InterWalletTransactionService interWalletTransactionService;

    private User sender;
    private User receiver;
    private Wallet senderWallet;
    private Wallet receiverWallet;

    @BeforeEach
    void setUp() {
        senderWallet = new Wallet(CurrencyType.USD);
        receiverWallet = new Wallet(CurrencyType.USD);
        senderWallet.setBalance(1000.0);
        receiverWallet.setBalance(500.0);

        sender = new User();
        sender.setId(1L);
        sender.setWallet(senderWallet);

        receiver = new User();
        receiver.setId(2L);
        receiver.setWallet(receiverWallet);
    }

    @Test
    void testTransferSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        interWalletTransactionService.transfer(1L, 2L, 100.0, CurrencyType.USD);

        assertEquals(900.0, sender.getWallet().getBalance());
        assertEquals(600.0, receiver.getWallet().getBalance());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(interWalletTransactionRepository, times(1)).save(any(InterWalletTransaction.class));
    }

    @Test
    void testTransferSenderNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            interWalletTransactionService.transfer(1L, 2L, 100.0, CurrencyType.USD);
        });

        assertEquals("Sender not found", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).findById(2L);
        verify(interWalletTransactionRepository, never()).save(any(InterWalletTransaction.class));
    }

    @Test
    void testTransferReceiverNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            interWalletTransactionService.transfer(1L, 2L, 100.0, CurrencyType.USD);
        });

        assertEquals("Receiver not found", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(interWalletTransactionRepository, never()).save(any(InterWalletTransaction.class));
    }

    @Test
    void testTransferInsufficientBalanceException() {
        Wallet mockSenderWallet = mock(Wallet.class);
        sender.setWallet(mockSenderWallet);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        doThrow(new InsufficientBalanceException("Insufficient balance")).when(mockSenderWallet).withdraw(anyDouble(), any(CurrencyType.class));

        Exception exception = assertThrows(InsufficientBalanceException.class, () -> {
            interWalletTransactionService.transfer(1L, 2L, 100.0, CurrencyType.USD);
        });

        assertEquals("Insufficient balance", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(interWalletTransactionRepository, never()).save(any(InterWalletTransaction.class));
    }

    @Test
    void testTransferFailedException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        doThrow(new TransferFailedException("Transfer failed")).when(interWalletTransactionRepository).save(any(InterWalletTransaction.class));

        Exception exception = assertThrows(TransferFailedException.class, () -> {
            interWalletTransactionService.transfer(1L, 2L, 100.0, CurrencyType.USD);
        });

        assertEquals("Transfer failed", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(interWalletTransactionRepository, times(1)).save(any(InterWalletTransaction.class));
    }

    @Test
    void testTransferInternalServerException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        doThrow(new RuntimeException("Internal server error")).when(interWalletTransactionRepository).save(any(InterWalletTransaction.class));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            interWalletTransactionService.transfer(1L, 2L, 100.0, CurrencyType.USD);
        });

        assertEquals("Internal server error", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(interWalletTransactionRepository, times(1)).save(any(InterWalletTransaction.class));
    }

    @Test
    void testTransferInvalidAmountException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        Exception exception = assertThrows(InvalidAmountException.class, () -> {
            interWalletTransactionService.transfer(1L, 2L, -100.0, CurrencyType.USD);
        });

        assertEquals("Withdrawal amount should be greater than 0", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(interWalletTransactionRepository, never()).save(any(InterWalletTransaction.class));
    }

    @Test
    void testTransferWalletNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        sender.setWallet(null);

        Exception exception = assertThrows(WalletNotFoundException.class, () -> {
            interWalletTransactionService.transfer(1L, 2L, 100.0, CurrencyType.USD);
        });

        assertEquals("Wallet not found", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(interWalletTransactionRepository, never()).save(any(InterWalletTransaction.class));
    }
}