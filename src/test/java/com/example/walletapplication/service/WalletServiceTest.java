package com.example.walletapplication.service;

import com.example.walletapplication.entity.Transaction;
import com.example.walletapplication.entity.User;
import com.example.walletapplication.entity.Wallet;
import com.example.walletapplication.exception.InsufficientBalanceException;
import com.example.walletapplication.exception.UserNotFoundException;
import com.example.walletapplication.repository.TransactionRepository;
import com.example.walletapplication.repository.UserRepository;
import com.example.walletapplication.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDepositSuccess() {
        Long userId = 1L;
        Double amount = 100.0;
        User user = new User();
        Wallet wallet = new Wallet();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(walletRepository.findByUser(user)).thenReturn(wallet);

        walletService.deposit(userId, amount);

        verify(walletRepository).save(wallet);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    public void testDepositUserNotFound() {
        Long userId = 1L;
        Double amount = 100.0;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> walletService.deposit(userId, amount));
    }

    @Test
    public void testWithdrawSuccess() {
        Long userId = 2L;
        Double amount = 100.0;
        User user = new User();
        Wallet wallet = new Wallet();
        wallet.deposit(200.0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(walletRepository.findByUser(user)).thenReturn(wallet);

        walletService.withdraw(userId, amount);

        verify(walletRepository).save(wallet);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    public void testWithdrawUserNotFound() {
        Long userId = 1L;
        Double amount = 100.0;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> walletService.withdraw(userId, amount));
    }

    @Test
    public void testWithdrawInsufficientBalance() {
        Long userId = 1L;
        Double amount = 100.0;
        User user = new User();
        Wallet wallet = mock(Wallet.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(walletRepository.findByUser(user)).thenReturn(wallet);
        doThrow(new InsufficientBalanceException("Insufficient balance")).when(wallet).withdraw(amount);

        assertThrows(InsufficientBalanceException.class, () -> walletService.withdraw(userId, amount));
    }

    @Test
    public void testTransferSuccess() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        Double amount = 100.0;
        User fromUser = new User();
        User toUser = new User();
        Wallet fromWallet = new Wallet();
        Wallet toWallet = new Wallet();
        fromWallet.deposit(200.0); // Ensure the fromWallet has enough balance

        when(userRepository.findById(fromUserId)).thenReturn(Optional.of(fromUser));
        when(userRepository.findById(toUserId)).thenReturn(Optional.of(toUser));
        when(walletRepository.findByUser(fromUser)).thenReturn(fromWallet);
        when(walletRepository.findByUser(toUser)).thenReturn(toWallet);

        walletService.transfer(fromUserId, toUserId, amount);

        verify(walletRepository, times(2)).save(any(Wallet.class));
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    public void testTransferUserNotFound() {
        Long fromUserId = 1L;
        Long toUserId = 2L;
        Double amount = 100.0;

        when(userRepository.findById(fromUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> walletService.transfer(fromUserId, toUserId, amount));
    }

    @Test
    public void testGetTransactionHistorySuccess() {
        Long userId = 1L;
        User user = new User();
        Wallet wallet = new Wallet();
        List<Transaction> transactions = Collections.emptyList();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(walletRepository.findByUser(user)).thenReturn(wallet);
        when(transactionRepository.findByWalletId(wallet.getId())).thenReturn(transactions);

        List<Transaction> result = walletService.getTransactionHistory(userId);

        assertNotNull(result);
        assertEquals(transactions, result);
    }

    @Test
    public void testGetTransactionHistoryUserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> walletService.getTransactionHistory(userId));
    }
}